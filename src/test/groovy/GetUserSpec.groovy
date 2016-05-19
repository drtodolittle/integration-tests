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
	
}