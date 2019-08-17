

public class Asset implements Comparable<Asset> {

    private int id;
    private double cost;
    private String date;

    public int getId(){
	return this.id;
    }

    public int getCost(){
	return this.cost;
    }

    public String getDate(){
	return this.date;
    }

    public void setId(int id){
	this.id = id;
    }

    public void setCost(int cost){
	this.cost = cost;
    }

    public void setDate(String date){
	this.date = date;
    }

    
    public void Asset(int id, int cost, String date){
	this.setId(id);
	this.setCost(cost);
	this.setDate(date);
    }


    public int compareTo(Asset a){

	if(a == null)
	    return -1;
	if(a == this)
	    return 1;
	if(this.id == a.id && this.cost == a.cost && this.date.equals(a.date))
	    return 1;
	return -1
    }

   
}