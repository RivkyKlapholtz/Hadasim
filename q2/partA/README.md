
# README.md

This system includes a partial server side, and a database for a corona database management system for a large health fund.
The system will present the members in the health insurance fund, and will manage the entry of the records in a database. In addition, the system will store key information regarding the corona epidemic in the context of the members of the health fund. In the future, they will be able to turn to this database in order to carry out various retrievals.

## Tech Stack

**Client:** Postman

**Server:** Java

**Database:** h2   (url: http://localhost:8080/h2-console)

## Installation

**Prerequisites**:

Java 8 or higher

Maven 3.0 or higher

Spring Boot 2.5.0 or higher

**1.** Clone the repository [https://github.com/RivkyKlapholtz/Hadasim/edit/main/q2/part](https://github.com/RivkyKlapholtz/Hadasim/tree/main/q2/partA/HMO)A

**2.** Go to URL http://localhost:8080/h2-console

**3.** Open SpringBootH2DatabaseExampleApplication java file and run it as Java Application

**4.** Download the Postman from https://www.getpostman.com/downloads/

Launch the Postman and Signup. Create a user name.

**5.** Open the Postman and do the following:

    1. Select the POST or GET
    2. Invoke the URL http://localhost:8080/the api you choosed....
**6.** Connect to h2: 

Open the browser and invoke the URL http://localhost:8080/h2-console. Click on the Connect button.

After clicking on the Connect button, you can see the tables in the database.

## API Reference

#### Returns a list of all members in the system

```http
  GET /members
```


#### Returns a specific member by his ID

```http
  GET /member/{id}
```


#### Returns the Corona object associated with a member ID

```http
  GET /member/{id}/corona
```
#### Returns a list of Vaccine objects associated with a member ID

```http
  GET /member/{id}/vaccines
```
#### Creates a new member in the system

```http
  POST /member
```
#### Creates or updates a Corona object associated with a member ID

```http
  POST /member/{id}/corona
```
#### Creates or updates a Vaccine object associated with a member ID

```http
  POST /member/{id}/vaccine
```
#### Uploads an image for a specific member

```http
  POST /uploading-image/{id}
```
#### Retrieves an image associated with a specific member

```http
  GET /members/{id}/image
```
#### Returns a graph of active patients over the last 30 days

```http
  GET /active-patients-last-month
```
#### Returns a count of members who have not received any vaccines

```http
  GET /members-notVaccinated
```
## Screenshots

What does h2 look like after running the project?

![image](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/35b86e00-51df-4e75-89f0-1eee8392d80d)

These are the tables created:

![11](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/97581717-b019-48ca-9c68-cc5942cc62ce)

now, go to Postman and run the various API commands:

![2](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/0bae1eb2-35fb-4809-acd9-e109d44c032f)

![3](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/55e1f89f-f152-438a-b409-c84a29fab1a8)

![4](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/5ca75bed-7351-465f-ba0f-9cd7c8f0388f)

![5](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/996a3158-cbf4-49cf-824a-d06e76a15398)

![6](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/e5ad1fbd-86b8-4c30-a50f-96d875530f4e)

(!) Note that the image you upload is located in:  C:\Users\User\Postman\files

![7](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/29e8eb47-1188-4a55-ae18-b54e49971646)

![8](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/4aecd4d2-a9d2-4344-9b78-ef72dee88326)

![9](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/8b949f6b-3abf-4941-9634-4e1a55fd9528)

![10](https://github.com/RivkyKlapholtz/Hadasim/assets/129298796/08437f26-fe61-48bb-9d16-b471e15244d8)



    
## Links that directed me

* https://www.javatpoint.com/spring-boot-h2-database
* https://start.spring.io/
* https://www.baeldung.com/spring-boot-h2-database
* https://www.geeksforgeeks.org/spring-boot-h2-database/

## Assumptions

* The cell phone field in the HMO member is optional, and can be NULL
* The rest of the fields in the HMO member are mandatory and cannot be NULL
* An example of the format for sending such an API request: 
```http
  POST /member
```
```http
 {
        "id": "666666666",
        "corona": {
            "sickDate": "2023-04-15",
            "recoveryDate": "2023-04-25"
        },
        "vaccines": [
            {
                "date": "2023-05-01",
                "manufacturer": "Novavax"
            },
            {
                "date": "2023-03-01",
                "manufacturer": "AstraZeneca"
            },
            {
                "date": "2023-02-01",
                "manufacturer": "AstraZeneca"
            }
        ],
        "image": null,
        "firstName": "Emily",
        "lastName": "Wang",
        "city": "Houston",
        "street": "Main Street",
        "housNumber": 789,
        "dayOfBirth": "1998-09-20",
        "phone": "5869234",
        "cellPhone": null
    }
```


## Bonus questions

1. Adding the ability to upload a photo of the checkout member on the client side and display it?

```http
   //in Member Class
   @Lob
   private byte[] image;

   public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    //in MemberController Class
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
```

2. Building an architectural specification of the system: the way of contacting the services
with the various APIs and a schematic view of the information in the database.
```http
  See separate file.
```


3. To present a summary view on the subject of the Corona virus:

* How many active patients were there each day in the last month (recommended to present as a graph)?
```http
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


```

* How many Copa members are not vaccinated at all?
```http
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
```
