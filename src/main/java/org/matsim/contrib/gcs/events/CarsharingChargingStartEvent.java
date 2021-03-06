package org.matsim.contrib.gcs.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.gcs.carsharing.CarsharingManager;
import org.matsim.contrib.gcs.carsharing.core.CarsharingStationMobsim;
import org.matsim.contrib.gcs.carsharing.core.CarsharingVehicleMobsim;

public class CarsharingChargingStartEvent extends AbstractCarsharingEvent {

	final Collection<ParkLog> parks;
	final String charging_time;
	final String type;
	
	public CarsharingChargingStartEvent(double eventTime, Scenario scenario, CarsharingManager manager, CarsharingStationMobsim station, Queue<CarsharingVehicleMobsim> roadtrain, double charging_time) {
		super(eventTime, scenario, manager, null, station);
		this.parks = new ArrayList<ParkLog>();
		if(roadtrain != null) {
			for(CarsharingVehicleMobsim v : roadtrain) {
				parks.add(new ParkLog(v.status().getPark()));
			}
		}
		//this.charging_time = CarsharingUtils.formatTime(year, month, day, df, charging_time);
		this.charging_time = String.valueOf(charging_time);
		this.type = "START";
	}

	@Override
	public String getEventType() {
		return this.getClass().getSimpleName();
	}
	
	
	// LOGGING
	@Override
	public Collection<Map<String, String>> getLogRows(double time) {
		ArrayList<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		for(ParkLog p : this.parks) {
			Map<String, String> logRow = new HashMap<String, String>();
			logRow.put("global.id", p.park_id);
			logRow.put("charging.dur", p.chargin_duration);
			logRow.put("vehicle.id", p.vehicle_id);
			logRow.put("fuel", p.vehicle_fuel);
			logRow.put("station.id", this.station_log.station_id);
			logRow.put("date", this.charging_time);
			logRow.put("lng", this.station_log.lng);
			logRow.put("lat", this.station_log.lat);
			logRow.put("type", this.type);
			rows.add(logRow);
		}
		return rows;
	}
		
	@Override
	public String getLogFile() {
		return manager.getConfig().getChargingLogFile(); // NEED TO BE CHANGED
	}
	
	@Override
	public String getLogType() {
		return "charging_events";
	}

	@Override
	public String getModuleName() {
		return getEventType();
	}

	@Override
	public void reset(int iteration) {
		
	}


}
