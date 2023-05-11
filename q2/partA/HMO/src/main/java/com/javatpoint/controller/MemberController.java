package com.javatpoint.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.javatpoint.model.Corona;
import com.javatpoint.model.Member;
import com.javatpoint.model.Vaccine;
import com.javatpoint.service.CoronaService;
import com.javatpoint.service.VaccineService;
import com.javatpoint.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@RestController
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    CoronaService coronaService;
    @Autowired
    VaccineService vaccineService;

    @GetMapping("/members")
    private List<Member> getAllMember() {
        return memberService.getAllMember();
    }

    @GetMapping("/member/{id}")
    private Member getMember(@PathVariable("id") String id) {
        return memberService.getMemberById(id);
    }

    @GetMapping("/member/{id}/corona")
    private Corona getCorona(@PathVariable("id") String id) {
        Corona c = memberService.getMemberById(id).getCorona();
        return c;
    }

    @GetMapping("/member/{id}/vaccines")
    private List<Vaccine> getVaccine(@PathVariable("id") String id) {
        List<Vaccine> list = memberService.getMemberById(id).getVaccines();
        return list;
    }

    @PostMapping("/member")
    private String saveMember(@RequestBody Member member) {
        String message = validationMember(member);
        if (message.equals("")) {
            coronaService.saveOrUpdate(member.getCorona());
            for (Vaccine v : member.getVaccines()) {
                vaccineService.saveOrUpdate(v);
            }
            memberService.saveOrUpdate(member);
        } else
            throw new IllegalArgumentException(message);
        return message;
    }

    @PostMapping("/member/{id}/corona")
    private String saveCorona(@RequestBody Corona corona, @PathVariable("id") String id) {
        Member m = memberService.getMemberById(id);
        m.setCorona(corona);
        return "";
    }

    @PostMapping("/member/{id}/vaccine")
    private String saveCorona(@RequestBody Vaccine vaccine, @PathVariable("id") String id) {
        String message = "";
        Member m = memberService.getMemberById(id);
        if (m.getVaccines().size() == 4)
            throw new IllegalArgumentException("Maximum 4 vaccinations per person, ");
        message = validationVaccine(vaccine);
        if (!message.equals(""))
            throw new IllegalArgumentException(message);
        m.getVaccines().add(vaccine);
        return message;
    }

    @PostMapping("/uploading-image/{id}")
    public String checkout(@PathVariable("id") String id, @RequestParam("image") MultipartFile image) throws IOException {
        Member member = memberService.getMemberById(id);
        member.setImage(image.getBytes());
        memberService.saveOrUpdate(member);
        return "upload_success";
    }

    @GetMapping("/members/{id}/image")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Member member = memberService.getMemberById(id);
        if (member.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(member.getImage().length);
            return new ResponseEntity<>(member.getImage(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active-patients-last-month")
    private String getActivePatientsLastMonth() {
        List<Integer> activePatients = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            int count = coronaService.getActivePatientsCountByDate(Date.valueOf(date));
            activePatients.add(count);
        }
        return drawGraph(activePatients);
    }

    @GetMapping("/members-notVaccinated")
    private int getNotVaccinatedMembers() {
        List<Member> allMembers = memberService.getAllMember();
        int count = 0;
        for (Member m : allMembers) {
            boolean vaccinated = false;
            for (Vaccine v : m.getVaccines()) {
                if (v.getDate() != null) {
                    vaccinated = true;
                    break;
                }
            }
            if (!vaccinated) {
                count++;
            }
        }
        return count;
    }

    private String validationMember(Member mem) {
        String message = "";
        //1
        if (mem.getId().length() != 9)
            message += "ID number should be 9 characters long, ";
        //2
        if (isNotDigitOnly(mem.getId()))
            message += "ID number should consist of numbers only, ";
        //3
        if (isNotLetterOnly(mem.getFirstName()))
            message += "First name should consist of letters only, ";
        //4
        if (isNotLetterOnly(mem.getLastName()))
            message += "Last name should consist of letters only, ";
        //5
        if (mem.getDayOfBirth().after(new java.util.Date()))
            message += "Invalid date of birth, ";
        //6
        if ((mem.getPhone().length() != 7 && mem.getPhone().length() != 9) || isNotDigitOnly(mem.getPhone()))
            message += "Invalid phone number, ";
        //7
        if (mem.getCellPhone() != null && (mem.getCellPhone().length() != 10 || isNotDigitOnly(mem.getCellPhone())))
            message += "Invalid cell-phone number, ";
        //8
        message += validationListVaccine(mem.getVaccines());
        //10
        message += validationCorona(mem.getCorona());
        if(String)
        return message;
    }

    private String validationVaccine(Vaccine v) {
        String message = "";
        if (!v.getManufacturer().toString().equals("Novavax") && !v.getManufacturer().toString().equals("Pfizer") && !v.getManufacturer().toString().equals("AstraZeneca") && !v.getManufacturer().toString().equals("Modern"))
            message += v.getManufacturer().toString() + " is not one of these: Pfizer, Modern ,AstraZeneca, Novavax, ";
        return message;
    }

    private String validationListVaccine(List<Vaccine> vac) {
        String message = "";
        for (Vaccine v : vac) {
            message += validationVaccine(v);
        }
        if (vac.size() > 4)
            message += "Maximum 4 vaccinations per person, ";
        return message;
    }

    private String validationCorona(Corona cor) {
        String message = "";
        if (cor.getRecoveryDate().before(cor.getRecoveryDate()))
            message += "The date of recovery should be after the date of receiving the positive result, ";
        return message;
    }

    private boolean isNotLetterOnly(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i)))
                return true;
        }
        return false;
    }

    private boolean isNotDigitOnly(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return true;
        }
        return false;
    }

    public static String drawGraph(List<Integer> data) {
        int max = Collections.max(data);
        StringBuilder sb = new StringBuilder();

        for (int i = max; i > 0; i--) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j) >= i) {
                    line.append("█ ");
                } else {
                    line.append("  ");
                }
            }
            sb.append(line.toString() + "\n");
        }

        for (int i = 0; i < data.size(); i++) {
            sb.append("--");
        }
        sb.append("\n");

        for (int i = 1; i <= data.size(); i++) {
            sb.append(i + " ");
        }
        sb.append("\n");

        return sb.toString();
    }


    /***********************************************
        From here on it's code for testing
     **********************************************/

    @GetMapping("/createMembers")
    public String createMembers() {

        Corona corona1 = new Corona();
        corona1.setSickDate(Date.valueOf("2022-01-01"));
        corona1.setRecoveryDate(Date.valueOf("2022-01-10"));
        coronaService.saveOrUpdate(corona1);
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setDate(Date.valueOf("2022-01-05"));
        vaccine1.setManufacturer(Vaccine.ManufactureType.Pfizer);
        vaccineService.saveOrUpdate(vaccine1);
        Member member1 = new Member();
        member1.setId("111111111");
        member1.setCorona(corona1);
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setCity("New York");
        member1.setStreet("5th Avenue");
        member1.setHousNumber(123);
        member1.setDayOfBirth(Date.valueOf("1990-01-01"));
        member1.setPhone("025874653");
        List<Vaccine> vaccines1 = new ArrayList<>();
        vaccines1.add(vaccine1);
        member1.setVaccines(vaccines1);
        memberService.saveOrUpdate(member1);

        Corona corona2 = new Corona();
        corona2.setSickDate(Date.valueOf("2022-02-15"));
        corona2.setRecoveryDate(Date.valueOf("2022-02-25"));
        coronaService.saveOrUpdate(corona2);
        Vaccine vaccine2 = new Vaccine();
        vaccine2.setDate(Date.valueOf("2022-03-01"));
        vaccine2.setManufacturer(Vaccine.ManufactureType.modern);
        vaccineService.saveOrUpdate(vaccine2);
        Member member2 = new Member();
        member2.setId("222222222");
        member2.setCorona(corona2);
        member2.setFirstName("Alice");
        member2.setLastName("Smith");
        member2.setCity("Chicago");
        member2.setStreet("Michigan Avenue");
        member2.setHousNumber(456);
        member2.setDayOfBirth(Date.valueOf("1985-05-10"));
        member2.setPhone("9453204");
        List<Vaccine> vaccines2 = new ArrayList<>();
        vaccines2.add(vaccine2);
        member2.setVaccines(vaccines2);
        memberService.saveOrUpdate(member2);

        Corona corona3 = new Corona();
        corona3.setSickDate(Date.valueOf("2021-12-01"));
        corona3.setRecoveryDate(Date.valueOf("2021-12-08"));
        coronaService.saveOrUpdate(corona3);
        Vaccine vaccine3 = new Vaccine();
        vaccine3.setDate(Date.valueOf("2021-12-15"));
        vaccine3.setManufacturer(Vaccine.ManufactureType.Pfizer);
        vaccineService.saveOrUpdate(vaccine3);
        Member member3 = new Member();
        member3.setId("333333333");
        member3.setCorona(corona3);
        member3.setFirstName("Bob");
        member3.setLastName("Johnson");
        member3.setCity("Houston");
        member3.setStreet("Main Street");
        member3.setHousNumber(789);
        member3.setDayOfBirth(Date.valueOf("1970-08-20"));
        member3.setPhone("085671254");
        List<Vaccine> vaccines3 = new ArrayList<>();
        vaccines3.add(vaccine3);
        member3.setVaccines(vaccines3);
        memberService.saveOrUpdate(member3);

        Corona corona4 = new Corona();
        corona4.setSickDate(Date.valueOf("2023-02-01"));
        corona4.setRecoveryDate(Date.valueOf("2023-02-10"));
        coronaService.saveOrUpdate(corona4);
        Vaccine vaccine4 = new Vaccine();
        vaccine4.setDate(Date.valueOf("2023-02-20"));
        vaccine4.setManufacturer(Vaccine.ManufactureType.AstraZeneca);
        vaccineService.saveOrUpdate(vaccine4);
        Member member4 = new Member();
        member4.setId("444444444");
        member4.setCorona(corona4);
        member4.setFirstName("Sarah");
        member4.setLastName("Lee");
        member4.setCity("San Francisco");
        member4.setStreet("Market Street");
        member4.setHousNumber(321);
        member4.setDayOfBirth(Date.valueOf("1995-10-18"));
        member4.setPhone("5691025");
        List<Vaccine> vaccines4 = new ArrayList<>();
        vaccines4.add(vaccine4);
        member4.setVaccines(vaccines4);
        memberService.saveOrUpdate(member4);

        Corona corona5 = new Corona();
        corona5.setSickDate(Date.valueOf("2023-03-15"));
        corona5.setRecoveryDate(Date.valueOf("2023-03-25"));
        coronaService.saveOrUpdate(corona5);
        Vaccine vaccine5 = new Vaccine();
        vaccine5.setDate(Date.valueOf("2023-04-01"));
        vaccine5.setManufacturer(Vaccine.ManufactureType.modern);
        vaccineService.saveOrUpdate(vaccine5);
        Member member5 = new Member();
        member5.setId("555555555");
        member5.setCorona(corona5);
        member5.setFirstName("Alex");
        member5.setLastName("Smith");
        member5.setCity("Chicago");
        member5.setStreet("Michigan Avenue");
        member5.setHousNumber(456);
        member5.setDayOfBirth(Date.valueOf("1985-05-10"));
        member5.setPhone("023258699");
        List<Vaccine> vaccines5 = new ArrayList<>();
        vaccines5.add(vaccine5);
        member5.setVaccines(vaccines5);
        memberService.saveOrUpdate(member5);

        Corona corona6 = new Corona();
        corona6.setSickDate(Date.valueOf("2023-04-15"));
        corona6.setRecoveryDate(Date.valueOf("2023-04-25"));
        coronaService.saveOrUpdate(corona6);
        Vaccine vaccine6 = new Vaccine();
        vaccine6.setDate(Date.valueOf("2023-05-01"));
        vaccine6.setManufacturer(Vaccine.ManufactureType.Novavax);
        vaccineService.saveOrUpdate(vaccine6);
        Vaccine vaccine61 = new Vaccine();
        vaccine61.setDate(Date.valueOf("2023-03-01"));
        vaccine61.setManufacturer(Vaccine.ManufactureType.AstraZeneca);
        vaccineService.saveOrUpdate(vaccine61);
        Vaccine vaccine62 = new Vaccine();
        vaccine62.setDate(Date.valueOf("2023-02-01"));
        vaccine62.setManufacturer(Vaccine.ManufactureType.AstraZeneca);
        vaccineService.saveOrUpdate(vaccine62);
        Member member6 = new Member();
        member6.setId("666666666");
        member6.setCorona(corona6);
        member6.setFirstName("Emily");
        member6.setLastName("Wang");
        member6.setCity("Houston");
        member6.setStreet("Main Street");
        member6.setHousNumber(789);
        member6.setDayOfBirth(Date.valueOf("1998-09-20"));
        member6.setPhone("095869022");
        List<Vaccine> vaccines6 = new ArrayList<>();
        vaccines6.add(vaccine6);
        vaccines6.add(vaccine61);
        vaccines6.add(vaccine62);
        member6.setVaccines(vaccines6);
        memberService.saveOrUpdate(member6);

        Corona corona7 = new Corona();
        corona7.setSickDate(Date.valueOf("2023-05-15"));
        corona7.setRecoveryDate(Date.valueOf("2023-05-25"));
        coronaService.saveOrUpdate(corona7);
        Vaccine vaccine7 = new Vaccine();
        vaccine7.setDate(Date.valueOf("2023-06-01"));
        vaccine7.setManufacturer(Vaccine.ManufactureType.AstraZeneca);
        vaccineService.saveOrUpdate(vaccine7);
        Member member7 = new Member();
        member7.setId("777777777");
        member7.setCorona(corona7);
        member7.setFirstName("David");
        member7.setLastName("Kim");
        member7.setCity("Dallas");
        member7.setStreet("Elm Street");
        member7.setHousNumber(246);
        member7.setDayOfBirth(Date.valueOf("1992-12-15"));
        member7.setPhone("4445556666");
        List<Vaccine> vaccines7 = new ArrayList<>();
        vaccines7.add(vaccine7);
        member7.setVaccines(vaccines7);
        memberService.saveOrUpdate(member7);

        return "7 rows of data inserted";
    }


}



