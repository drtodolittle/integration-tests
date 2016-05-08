import spock.lang.Specification
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*

class GetToDoListSpec extends Specification {

	def serverurl = 'http://app.drtodolittle.de'
//	def serverurl = 'http://localhost:2000'

	
	def "Get ToDo-List"() {
		
		given: "A RESTClient with an user who is logged on"
			def drtodolittle = new RESTClient( serverurl )
			def response = drtodolittle.post( path : '/api/todos/login',
				contentType: JSON,
				requestContentType:  JSON,
				body: [email: "dirk", password: "dirk1234"])
			assert response.status == 200
			def token = response.data.token
		
		when: "request all todos per GET Method"
			response = drtodolittle.get( path: '/api/todos',
			contentType: JSON,
			requestContentType: JSON,
			headers: [authorization: "bearer " + token])
			
		then: "the server send the todos as JSON"
			response.status == 200
	}

	def "Get ToDo-List without login"() {
		
		given: "A RESTClient without login"
			def drtodolittle = new RESTClient( serverurl )
			def token = ""
		
		when: "request all todos per GET Method"
			response = drtodolittle.get( path: '/api/todos',
			contentType: JSON,
			requestContentType: JSON,
			headers: [authorization: "bearer " + token])
			
		then: "the server send an unauthorized status"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}

	def "Get ToDo-List with corrupt login token"() {
		
		given: "A RESTClient with corrupt login token"
			def drtodolittle = new RESTClient( serverurl )
			def token = "eyJhbGciOzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicHJvY2Vzc0lkIjoiMTIzNCIsImZpcnN0bmFtZSI6ImZpcnN0dGVzdCIsImxhc4RuYW1lIjoibGFzdHRlc3QifQ.izoHafVk9lONcigE3SbGNXkveC6Xj2uJkOaZ8TmgtS-a3db5L2usLOoo3wm4s_PUKR_x0rlgEscMHs9V0vwG6A"
		
		when: "request all todos per GET Method"
			response = drtodolittle.get( path: '/api/todos',
			contentType: JSON,
			requestContentType: JSON,
			headers: [authorization: "bearer " + token])
			
		then: "the server send an unauthorized status"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}
	
	def "Get ToDo-List with wrong login token"() {
		
		given: "A RESTClient with wrong login token"
			def drtodolittle = new RESTClient( serverurl )
			def token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwicHJvY2Vzc0lkIjoiMTIzNCIsImZpcnN0bmFtZSI6ImZpcnN0dGVzdCIsImxhc4RuYW1lIjoibGFzdHRlc3QifQ.izoHafVk9lONcigE3SbGNXkveC6Xj2uJkOaZ8TmgtS-a3db5L2usLOoo3wm4s_PUKR_x0rlgEscMHs9V0vwG6A"
		
		when: "request all todos per GET Method"
			response = drtodolittle.get( path: '/api/todos',
			contentType: JSON,
			requestContentType: JSON,
			headers: [authorization: "bearer " + token])
			
		then: "the server send an unauthorized status"
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 401
	}
	
	def "Get ToDo-List with wrong requestContentType"() {
		
		given: "A RESTClient with an user who is logged on"
			def drtodolittle = new RESTClient( serverurl )
			def response = drtodolittle.post( path : '/api/todos/login',
				contentType: JSON,
				requestContentType:  JSON,
				body: [email: "dirk", password: "dirk1234"])
			assert response.status == 200
			def token = response.data.token
		
		when: "request all todos per GET Method"
			response = drtodolittle.get( path: '/api/todos',
			contentType: 'text/xml',
			headers: [authorization: "bearer " + token])
			
		then: "the server send an unsupported media type status"
			println response.data
			def e = thrown(groovyx.net.http.HttpResponseException)
			e.getStatusCode() == 406
	}
	
}