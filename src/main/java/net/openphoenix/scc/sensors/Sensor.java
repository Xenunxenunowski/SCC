package net.openphoenix.scc.sensors;

public class Sensor {
    private String jsonValue;
    private String UUID;
    private String sensorType;
    private String id;
    private String timestamp;

    public Sensor(String jsonValue, String uuid, String sensorType,String id,String timestamp) {
        this.jsonValue = jsonValue;
        this.timestamp =timestamp;
        this.id =id;
        this.UUID = uuid;
        this.sensorType = sensorType;
    }
    public Sensor(){

    }


    public String getUUID() {
        return UUID;
    }


    public String getSensorType() {
        return sensorType;
    }


    public String getJsonValues() {
        return jsonValue;
    }
}
