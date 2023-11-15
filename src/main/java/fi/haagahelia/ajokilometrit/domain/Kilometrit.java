package fi.haagahelia.ajokilometrit.domain;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Kilometrit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String date;
	private String car;
	private int odometerReading;
	private double litersFueled;
	private double price;
	private int drivenSinceLast;
	private double averageKilometersToLiters;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fuel_id")
	private Fuel fuel;

	public Kilometrit() {
	}

	public Kilometrit(String date, String car, int odometerReading, double litersFueled, double price,
			KilometritRepository kilometritRepository) {
		this.date = date;
		this.car = car;
		this.odometerReading = odometerReading;
		this.litersFueled = litersFueled;
		this.price = price;
		this.calculateDerivedFields(kilometritRepository);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public int getOdometerReading() {
		return odometerReading;
	}

	public void setOdometerReading(int odometerReading) {
		this.odometerReading = odometerReading;
	}

	public double getLitersFueled() {
		return litersFueled;
	}

	public void setLitersFueled(double litersFueled) {
		this.litersFueled = litersFueled;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Fuel getFuel() {
		return fuel;
	}

	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
	}

	public int getDrivenSinceLast() {
		return drivenSinceLast;
	}

	public void setDrivenSinceLast(int drivenSinceLast) {
		this.drivenSinceLast = drivenSinceLast;
	}

	public double getAverageKilometersToLiters() {
		return averageKilometersToLiters;
	}

	public void setAverageKilometersToLiters(double averageKilometersToLiters) {
		this.averageKilometersToLiters = averageKilometersToLiters;
	}

	public void calculateDerivedFields(KilometritRepository kilometritRepository) {
	    List<Kilometrit> allEntries = new ArrayList<>();
	    kilometritRepository.findAll().forEach(allEntries::add);

	    System.out.println("DEBUG: Number of entries before calculation: " + allEntries.size());

	    if (!allEntries.isEmpty()) {
	        Kilometrit lastKilometrit = allEntries.get(allEntries.size() - 1);
	        this.drivenSinceLast = this.odometerReading - lastKilometrit.getOdometerReading();
	    } else {
	        this.drivenSinceLast = 0;
	    }

	    this.averageKilometersToLiters = (this.litersFueled != 0) ? this.drivenSinceLast / this.litersFueled : 0;

	    kilometritRepository.save(this);

	    // Debug information
	    allEntries.clear();
	    kilometritRepository.findAll().forEach(allEntries::add);

	    System.out.println("DEBUG: Number of entries after calculation: " + allEntries.size());
	}


}
