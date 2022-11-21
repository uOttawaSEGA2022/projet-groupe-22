public class Meal {
	
	private String chefUid ;
	private String mealName;
	private String mealType;
	private String gastronomyType;
	private double price;
	
	
	public Meal (String chefUid,String mealName,String mealType,String gastronomyType,double price){
		this.chefUid = chefUid ;
		this.mealName = mealName ;
		this.mealType = mealType ;
		this.gastronomyType = gastronomyType ;
		this.price = price ;		
	}
	
	//Setters
	public void setChef (String chefUid){
		this.chefUid = chefUid ;
	}
	
	public void setMealName(String mealName){
		this.mealName = mealName ;
	}
	
	public void setMealType(String mealType){
		this.mealType = mealType ;
	}
	public void setGastronomyType(String gastronomyType){
		this.gastronomyType = gastronomyType ;
	}
	public void setPrice(double price){
		this.price = price ;	
	}
	
	//Getters
	public String getChefUid(){
		return this.chefUid;
	}
	public String getMealName(){
		return this.mealName;
	}
	public String getMealType(){
		return this.mealType;
	}
	public String getGastronomyType(){
		return this.gastronomyType;
	}
	public double getPrice(){
		return this.price;
	}
	

	
	
}
