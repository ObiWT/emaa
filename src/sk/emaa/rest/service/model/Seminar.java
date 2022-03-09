package sk.emaa.rest.service.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("seminar")
public class Seminar {
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	@Id
	private String id;
	private Date startDate;
	private Date endDate;
	private String location;
	private List<SeminarDay> days;
	private int daysAccomodation;
    private double priceTraining;
	private double priceAccomodation;
	private double priceBreakfast;
	private double priceLunch;
	private double priceDinner;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<SeminarDay> getDays() {
		return days;
	}

	public void setDays(List<SeminarDay> days) {
		this.days = days;
	}

	public int getDaysAccomodation() {
		return daysAccomodation;
	}

	public void setDaysAccomodation(int daysAccomodation) {
		this.daysAccomodation = daysAccomodation;
	}

	public double getPriceTraining() {
		return priceTraining;
	}

	public void setPriceTraining(double priceTraining) {
		this.priceTraining = priceTraining;
	}

	public double getPriceAccomodation() {
		return priceAccomodation;
	}

	public void setPriceAccomodation(double priceAccomodation) {
		this.priceAccomodation = priceAccomodation;
	}

	public double getPriceBreakfast() {
		return priceBreakfast;
	}

	public void setPriceBreakfast(double priceBreakfast) {
		this.priceBreakfast = priceBreakfast;
	}

	public double getPriceLunch() {
		return priceLunch;
	}

	public void setPriceLunch(double priceLunch) {
		this.priceLunch = priceLunch;
	}

	public double getPriceDinner() {
		return priceDinner;
	}

	public void setPriceDinner(double priceDinner) {
		this.priceDinner = priceDinner;
	}
	
	@Override
	public String toString() {
		return "id: " + id + ", location:" + location + ", startDate:" + sdf.format(startDate) + ", endDate:" + sdf.format(endDate);
	}

}
