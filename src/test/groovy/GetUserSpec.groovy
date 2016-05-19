import spock.lang.Specification
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*

class GetUserSpec extends Specification {

	def serverurl = 'http://app.drtodolittle.de'
//	def serverurl = 'http://localhost:2000'
	
	def "User get account infos"() {
		
		given: "A RESTClient with an user who is logged on"
			def drtodolittle = new RESTClient( serverurl )
			def response = drtodolittle.post( path : '/api/todos/login',
				contentType: JSON,
				requestContentType:  JSON,
				body: [email: "dirk", password: "dirk1234"])
			assert response.status == 200
			def token = response.data.token
		
		when: "gets the user profile"
			response = drtodolittle.get( path : '/api/user',
			contentType: JSON,
			requestContentType:  JSON,
			headers: [authorization: "bearer " + token])
			
		then: "Server send status code 200 and some uswerinfos"
			response.status == 200
			response.data.id == "dirk"
 	}

	
	def "User logon with new password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: [email: "dirk", password: "dirk1234new"])
			
		then: "Server send a valid JSON Web Token"
			response.status == 200
			response.data.token.size() > 0
	}

	def "User logon with wrong password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: [email: "dirk", password: "dirk1234"])
			
		then: "Server send an unauthorized exception"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}
	
	def "User changes his password to old one"() {
		
		given: "A RESTClient with an user who is logged on"
			def drtodolittle = new RESTClient( serverurl )
			def response = drtodolittle.post( path : '/api/todos/login',
				contentType: JSON,
				requestContentType:  JSON,
				body: [email: "dirk", password: "dirk1234new"])
			assert response.status == 200
			def token = response.data.token
		
		when: "Send oldpassword and newpassword per PUT Method"
			response = drtodolittle.put( path : '/api/user',
			contentType: JSON,
			requestContentType:  JSON,
			body: [oldPassword: "dirk1234new", newPassword: "dirk1234"],
			headers: [authorization: "bearer " + token])
			
		then: "Server send status code 204"
			response.status == 204
	}
	
}