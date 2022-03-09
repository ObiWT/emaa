package sk.emaa.rest.service;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.User;

@Service
@Path("/userService")
public class UserService {
	
	private Datastore datastore;
	
	public UserService() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		datastore = morphia.createDatastore(mongoClient, "emaa");
		datastore.ensureIndexes();
	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		
	    List<User> users = datastore.find(User.class).find().toList();
	    
	    System.out.println("getUsers(): " + users);

        return Response.status(200).entity(users).build();
	}
	
	@POST
	@Path("/users/authenticate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		
		System.out.println("UserService.authenticate() - username: " + username + ", password: " + password);
		
		MorphiaCursor<User> cursor = datastore.find(User.class).filter("username", username).filter("password", password).find();
		
		if (cursor.hasNext()) {
			User user = cursor.next();
			return Response.status(200).entity(user).build();			
		} else {
			return Response.status(401).build();
		}

	}
	
}
