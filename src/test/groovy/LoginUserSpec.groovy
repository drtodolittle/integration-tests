import spock.lang.Specification
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*

class LoginUserSpec extends Specification {

	def serverurl = 'http://app.drtodolittle.de'
//	def serverurl = 'http://localhost:2000'
	
	def "User logon with correct username and password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: [email: "dirk", password: "dirk1234"])
			
		then: "Server send a valid JSON Web Token"
		
			response.status == 200
			response.data.token.size() > 0
	
	}

	
	def "User logon with incorrect username and password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: [email: "dirk1", password: "dirk12345"])
			
		then: "Server send an unauthorized exception"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}
	

	def "User logon with empty username and password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: [email: "", password: ""])
			
		then: "Server send an unauthorized exception"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}

	def "User logon with no username and password"() {
		
		given: "New RESTClient without login token"
			def drtodolittle = new RESTClient( serverurl )
		
		when: "Send username and password per POST Method"
			def response = drtodolittle.post( path : '/api/todos/login',
			contentType: JSON,
			requestContentType:  JSON,
			body: null)
			
		then: "Server send an unauthorized exception"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}
	
}