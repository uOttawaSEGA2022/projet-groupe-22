public class Cook extends User {

    private String name;
    private String lastName;

    private String adress;


    /**
     * @param name
     * @param lastName
     * @param adress
     * @param email
     * @param password
     */
    public Cook(String name,String lastName,String adress,String email , String password) {
        super(email ,password);
        this.name = name;
        this.lastName = lastName;
        this.adress=adress;
    }
    /**
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return
     */
    public String getLastName(){
        return this.LastName;
    }
    /**
     * @return
     */
    public String getAdress(){
        return this.adress;
    }

    /**
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param lastName
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAdress(String adress){
        this.adress = adress;
    }
    public Cook() {
    }



}
