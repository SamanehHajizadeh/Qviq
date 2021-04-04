Download code Or just clone it
Go to project path in terminal and run: mvn spring-boot:run

Follow as below:
In the postman or URL send these Requests :

1. Set up a spring boot project with gradle or maven as your build tool,
   have it return "Hello World!" with status 200 on GET to /

 * http://localhost:8080/
 ------------------------------------------------------------------------
 2. Create a @Repository with
 getLog(int logId) and
 addMessage(String name, int logId, String info) methods that maps to an internal data object.
 Each info that gets added to a log should also get
 a timestamp added to it.
 The "name" field is intended to identify the submitting party.

  ------------------------------------------------------------------------
  3. Add rest methods for the functionality described in step 2.

  * http://localhost:8080/all
  * http://localhost:8080/getLog
  * http://localhost:8080/addMessage?name=samane&logId=10&info=First_test
  -----------------------------------------------------------------------------


