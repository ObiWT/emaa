package sk.emaa.rest.service;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.mongodb.MongoClient;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.User;

public abstract class AbstractService {

	boolean isAuthorized(HttpHeaders headers) {
		
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		// size could change in the future
		if (authHeaders.size() != 1 ) {
			return false;
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		Datastore datastore = morphia.createDatastore(mongoClient, "emaa");
		datastore.ensureIndexes();
		
		MorphiaCursor<User> cursor = datastore.find(User.class).filter("token", authHeaders.get(0)).find();
		//if (cursor.hasNext())
		//User user = datastore.find(User.class).filter("token", authHeaders.get(0)).find().next();
		
		// found something?
		return cursor.hasNext();
	}
	
}
