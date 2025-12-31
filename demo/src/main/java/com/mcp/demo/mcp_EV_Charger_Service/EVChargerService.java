package com.mcp.demo.mcp_EV_Charger_Service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

//import org.springframework.ai.model.function.FunctionCallback;

@Service
public class EVChargerService {

    public record ChargingRecord(String timestamp,String chargerId,String vechicleId,double powerDrawKw,double transformerLoadKw,String chargingStatus){

    }
        //use a concurrent hashmap to store the charging records in memory
        private static final Map<String,ChargingRecord> chargingRecords=new ConcurrentHashMap<>();
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/dd/yyyy HH:mm:ss");

        public EVChargerService(){
            addChargingRecord("9/4/2025 8:00", "EVCH001","EV123",7.2,85,"Charging");
            addChargingRecord("9/4/2025 8:10", "EVCH001","EV123",7.4,86.5,"Charging");
            addChargingRecord("9/4/2025 8:20", "EVCH001","EV123",7.3,87.2,"Charging");

        }

    @Tool(name="addChargingRecord", description="Add a new charging record to the EV CHARGER system. Specify timestamp, chargerId, vehicleId, power draw (KW), transformer load (KW), and charging status.")
    public String addChargingRecord(String timestamp, String chargerId, String vehicleId, double powerDrawKw, double transformerLoadKw, String chargingStatus){
        if(chargerId==null || chargerId.trim().isEmpty()){
            return "Error : Invalid charger id";
        }
        if(vehicleId==null || vehicleId.trim().isEmpty()){
                return "Error : Invalid vehicle id";
            }
        if(powerDrawKw<0 || transformerLoadKw<0 ){
        return "Error : Invalid power draw kw";
        }
        String timestampToUse=timestamp;
        if(timestamp==null || timestamp.trim().isEmpty()){
            timestampToUse= LocalDateTime.now().format(dtf);
        }

        String key=chargerId +"_" + vehicleId + "_" + timestampToUse;
        ChargingRecord record=new ChargingRecord(timestampToUse, chargerId, vehicleId, powerDrawKw, transformerLoadKw, chargingStatus!=null ? chargingStatus : "Unknown");

        chargingRecords.put(key, record);
        return "Added charging record for charger: " + chargerId + ", vehicle: " + vehicleId + " at " + timestampToUse;
        }

        @Tool(name="getAllChargingRecords", description="Get all charging records from the EV CHARGER system.")
        public List<ChargingRecord> getAllChargingRecords(){
            return new ArrayList<>(chargingRecords.values());
        }

        @Tool(name="getRecordByCharger",description="Get all charging records for a specific charger ID,Specify the charger ID to filter records.")
        public List<ChargingRecord>getRecordByCharger(String chargerId){
            if(chargerId==null || chargerId.trim().isEmpty()){
                return new ArrayList<>();
            }
            return chargingRecords.values().stream().filter(r->r.chargerId().equalsIgnoreCase(chargerId)).collect(Collectors.toList());

        }

        @Tool(name="getRecordByVehicle",description="Get all charging records for a specific vehicle ID,Specify the vehicle ID to filter records.")
        public List<ChargingRecord>getRecordByVehicle(String vehicleId){
            if(vehicleId==null || vehicleId.trim().isEmpty()){
                return new ArrayList<>();
            }
            return chargingRecords.values().stream().filter(r->r.vechicleId().equalsIgnoreCase(vehicleId)).collect(Collectors.toList());
        }

        @Tool(name="getRecordByStatus",description="Get all charging records for a specific charging status,Specify the charging status to filter records.")
        public List<ChargingRecord>getRecordByStatus(String chargingStatus){
            if(chargingStatus==null || chargingStatus.trim().isEmpty()){
                return new ArrayList<>();
            }
            return chargingRecords.values().stream().filter(r->r.chargingStatus().equalsIgnoreCase(chargingStatus)).collect(Collectors.toList());
        }

        @Tool(name="deleteChargingRecord", description="Delete a charging record from the EV CHARGER system. Specify charger ID, vehicle ID, and timestamp to identify the record.")
        public String deleteChargingRecord(String chargerId, String vehicleId, String timestamp){
            if(chargerId==null || chargerId.trim().isEmpty()){
                return "Error : Invalid charger id";
            }
            String key=chargerId + "_" + vehicleId + "_" + timestamp;
            ChargingRecord removed=chargingRecords.remove(key);
            if(removed!=null){
                return "Deleted charging record for charger: " + chargerId + ", vehicle: " + vehicleId + " at " + timestamp;    

            }
            return "Deleting charging record failed: Record not found.";

        }

}


