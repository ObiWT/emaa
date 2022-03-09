package sk.emaa.rest.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import sk.emaa.rest.service.model.School;
import sk.emaa.rest.service.model.SchoolDetail;
import sk.emaa.rest.service.model.Student;
import sk.emaa.rest.service.model.Training;

@Path("/schoolService")
public class SchoolService extends AbstractService {
	
	private MongoClient mongoClient;
	private Datastore datastore;
	
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public SchoolService() {
		mongoClient = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		morphia.mapPackage("sk.emaa.rest.service.model");
		datastore = morphia.createDatastore(mongoClient, "emaa");
	}
	
	@GET
	@Path("/schools")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSchools(@Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
	    List<School> schools = datastore.createQuery(School.class).find().toList();

        return Response.status(200).entity(schools).build();
	}
	
	@GET
	@Path("/detail/{schoolId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDetail(@PathParam("schoolId") String schoolId, @Context HttpHeaders headers) {
		
		if (!isAuthorized(headers)) {
			return Response.status(401).build();
		}
		
		SchoolDetail detail = new SchoolDetail();
		
		// set average
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		
		YearMonth startYearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = startYearMonth.atDay(1);
		
		Date startDate = java.util.Date.from(firstDayOfMonth.atStartOfDay()
	      	      .atZone(ZoneId.systemDefault())
	      	      .toInstant());
	    
	    List<Training> trainings = datastore.find(Training.class)
	    		.filter("schoolId", schoolId)
	    		.filter("date >=", startDate)
	    		.filter("date <=", new Date())
	    		.find().toList();
	    
	    Collections.sort(trainings); // order by date - but not needed, because I'm doing sorting later
	    
	    long studentCount = 0;
		if (trainings.isEmpty()) {
			// return Response.status(200).entity(detail).build();
			detail.setAvarageOnTraining("0");
		} else {
			Map<String, Integer> data = new LinkedHashMap<>();
			Collections.sort(trainings); // order by date
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
			
			for (Training training : trainings) {
				studentCount = studentCount + training.getPresentStudentIds().size();
				// set data
				cal.setTime(training.getDate());
				data.put(cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH ) + 1) + ".", training.getPresentStudentIds().size());
			}
			
			detail.setAvarageOnTraining(df.format( (double) studentCount / trainings.size()) );
			detail.setData(data);
		}
		
		// set counts
		long activeStudents = datastore.find(Student.class).filter("schoolId", schoolId).filter("deactivated", false).count();
		long inactiveStudents = datastore.find(Student.class).filter("schoolId", schoolId).filter("deactivated", true).count();
	    detail.setActiveStudentsCount(activeStudents);
	    detail.setInactiveStudentsCount(inactiveStudents);
	    
        return Response.status(200).entity(detail).build();
	}
	
}
