import spock.lang.Specification
import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*

class GetToDoListSpec extends Specification {

	
	def "Get ToDo-List"() {
		
		given: "A RESTClient with an user who is logged on"
			def drtodolittle = new RESTClient( 'http://www.drtodolittle.de' )
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
	
}