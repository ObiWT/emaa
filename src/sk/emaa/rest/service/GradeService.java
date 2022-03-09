package sk.emaa.rest.service;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import sk.emaa.rest.service.model.Grade1;
import sk.emaa.rest.service.model.Grade2;
import sk.emaa.rest.service.model.Grade3;

@Path("/gradeService")
public class GradeService extends AbstractService {

	@GET
	@Path("/grade1/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGrade1(@PathParam("studentId") String studentId, @Context HttpHeaders headers) {
		
		System.out.println("getGrade1() called with studentId: " + studentId);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		Datastore datastore = morphia.createDatastore(mongoClient, "emaa");
		
	    Grade1 grade1 = new Grade1();
	    MorphiaCursor<Grade1> cursor = datastore.find(Grade1.class).filter("studentId", studentId).find();
	    if (cursor.hasNext()) {
	    	grade1 = cursor.next();
	    }
	    
	    System.out.println("Grade1: " + grade1.toString());
	
	    return Response.status(200).entity(grade1).build();
	}
	
	@POST
	@Path("/grade1")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveGrade1(Grade1 grade1, @Context HttpHeaders headers) {
		
		System.out.println("saveGrade1(): " + grade1);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("emaa");
		MongoCollection<Document> grade1Collection = database.getCollection("grade1");		
		
		Bson filter = eq("studentId", grade1.getStudentId());
		Bson update1 = set("siuNimTao", grade1.getSiuNimTao());
		Bson update2 = set("selfdefense", grade1.getSelfdefense());
		Bson update3 = set("basicTechniques", grade1.getBasicTechniques());
		Bson update4 = set("latSao", grade1.getLatSao());
		Bson update5 = set("theory", grade1.getTheory());
		Bson updates = combine(update1, update2, update3, update4, update5);
		UpdateOptions options = new UpdateOptions().upsert(true);
		UpdateResult updateResult = grade1Collection.updateOne(filter, updates, options);
		
        System.out.println(updateResult);
        
        mongoClient.close();
        
		return Response.status(200).build();
	}
	
	@GET
	@Path("/grade2/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGrade2(@PathParam("studentId") String studentId, @Context HttpHeaders headers) {
		
		System.out.println("getGrade2() called with studentId: " + studentId);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		Datastore datastore = morphia.createDatastore(mongoClient, "emaa");
		
	    Grade2 grade2 = new Grade2();
	    MorphiaCursor<Grade2> cursor= datastore.find(Grade2.class).filter("studentId", studentId).find();
	    if (cursor.hasNext()) {
	    	grade2 = cursor.next();
	    }
	    
	    System.out.println("Grade2: " + grade2.toString());
	
	    return Response.status(200).entity(grade2).build();
	}
	
	@POST
	@Path("/grade2")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveGrade2(Grade2 grade2, @Context HttpHeaders headers) {
		
		System.out.println("saveGrade2(): " + grade2);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("emaa");
		MongoCollection<Document> grade2Collection = database.getCollection("grade2");		
		
		Bson filter = eq("studentId", grade2.getStudentId());
		Bson update1 = set("chumKiu", grade2.getChumKiu());
		Bson update2 = set("history", grade2.getHistory());
		Bson update3 = set("danChi", grade2.getDanChi());
		Bson update4 = set("chiSaoSection1", grade2.getChiSaoSection1());
		Bson updates = combine(update1, update2, update3, update4);
		UpdateOptions options = new UpdateOptions().upsert(true);
		UpdateResult updateResult = grade2Collection.updateOne(filter, updates, options);
		
        System.out.println(updateResult);
        
		mongoClient.close();
		
		return Response.status(200).build();
	}
	
	@GET
	@Path("/grade3/{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGrade3(@PathParam("studentId") String studentId, @Context HttpHeaders headers) {
		
		System.out.println("getGrade3() called with studentId: " + studentId);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		Datastore datastore = morphia.createDatastore(mongoClient, "emaa");
		
	    Grade3 grade3 = new Grade3();
	    MorphiaCursor<Grade3> cursor= datastore.find(Grade3.class).filter("studentId", studentId).find();
	    if (cursor.hasNext()) {
	    	grade3 = cursor.next();
	    }
	    
	    System.out.println("Grade3: " + grade3.toString());
	
	    return Response.status(200).entity(grade3).build();
	}
	
	@POST
	@Path("/grade3")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveGrade3(Grade3 grade3, @Context HttpHeaders headers) {
		
		System.out.println("saveGrade3(): " + grade3);
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("emaa");
		MongoCollection<Document> grade3Collection = database.getCollection("grade3");
		
		Bson filter = eq("studentId", grade3.getStudentId());
		Bson update1 = set("defenseAgainstMultipleEnemies", grade3.getDefenseAgainstMultipleEnemies());
		Bson update2 = set("defenseAgainstWeapons", grade3.getDefenseAgainstWeapons());
		Bson update3 = set("basicTechniques", grade3.getSoftTechniques());
		Bson updates = combine(update1, update2, update3);
		UpdateOptions options = new UpdateOptions().upsert(true);
		UpdateResult updateResult = grade3Collection.updateOne(filter, updates, options);
		
        System.out.println(updateResult);
        
		mongoClient.close();
		
		return Response.status(200).build();
	}
	
}
