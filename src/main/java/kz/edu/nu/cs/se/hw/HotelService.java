package kz.edu.nu.cs.se.hw;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/items")
public class HotelService {

    private List<String> passwords = new CopyOnWriteArrayList<String>();
    private List<String> logins = new CopyOnWriteArrayList<String>();




    String accUsername;
    String accPassword;
    String position = "User";

    int hotelId;

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    int guestId;
    int employeeId;
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }



    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public HotelService(String accUsername) {
        accUsername = "Cuckold";
    }

    public String getAccUsername() {
        return accUsername;
    }

    public void setAccUsername(String accUsername) {
        this.accUsername = accUsername;
    }

    public HotelService() {
        for (int i = 0; i < 20; i++) {
            passwords.add("parol" + String.valueOf(i));
            logins.add("login" + String.valueOf(i));
        }

    }

    @GET
    @Path("/login")
    public Response logIn(@QueryParam("username") String name, @QueryParam("password") String pass) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.guest where Username = '" + name +"';");
            while(resultSet.next()){
                a++;
                if(pass.equals(resultSet.getString("Password"))){
                    found = true;
                    position = "guest";
                    guestId = resultSet.getInt("Guest_ID");
                    break;
                }
                else{
                    break;
                }
            }
            if(a == 0){
                ResultSet rSet = statement.executeQuery("select * from mydb.employee where Username = '" + name +"';");
                while(rSet.next()){
                    a++;
                    if(pass.equals(rSet.getString("Password"))){
                        found = true;
                        position = rSet.getString("Position");
                        hotelId = rSet.getInt("Hotel_Hotel_ID");
                        employeeId = rSet.getInt("EmployeeID");
                        break;
                    }
                    else{
                        break;
                    }
                }
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
                System.out.println(e.toString());
                r.setResponse(e.toString());
                json = gson.toJson(r, ResponseHelper.class);
                return Response.ok(json).build();
            }
        }


        if(found){
            r.setResponse(position);
            setAccUsername(name);
            accUsername = name;
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }
        else{
            r.setResponse("error");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

    }

    @GET
    @Path("/userinfo")
    public Response userInfo() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<String> res = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.guest where Username = '" + accUsername +"';");
            while(resultSet.next()){
                a++;
                res.add("Guest ID: " + String.valueOf(resultSet.getInt("Guest_ID")));
                res.add("Name: " + resultSet.getString("Name"));
                res.add("Surname: " + resultSet.getString("Surname"));
                res.add("Username: " + resultSet.getString("Username"));
                res.add("Email: " + resultSet.getString("Email"));
                res.add("Identification Type: " + resultSet.getString("Identification_type"));
                res.add("Identification Number: " + String.valueOf(resultSet.getString("Identification_number")));
                res.add("Address: " + resultSet.getString("Address"));
                res.add("Mobile Phone: " + resultSet.getString("Mobile_phone_#"));
                res.add("Home Phone: " + resultSet.getString("Home_Phone_#"));
                res.add("Status: " + resultSet.getString("Guest_type_Guest_type"));
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(res)).build();

    }

    @GET
    @Path("/user")
    public Response user() {



        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        r.setResponse(accUsername);


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/hotelname")
    public Response hotelName() throws ClassNotFoundException, SQLException {



        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();
        String hName = "";

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");

        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from hotel h where h.Hotel_ID = " + hotelId + ";");
            while(resultSet.next()){
                hName = resultSet.getString("Name");
            }

        }

        r.setResponse(hName);


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @Path("/signout")
    public Response signOut() {



        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        position = "User";
        r.setResponse(position);


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/role")
    public Response role() {



        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        r.setResponse(position);


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    public static class ParameterBean {
        @QueryParam("FirstName")
        public String fname;

        @QueryParam("SecondName")
        public String sname;

        @QueryParam("Email")
        public String email;

        @QueryParam("MobilePhone")
        public String mphone;

        @QueryParam("Username")
        public String name;

        @QueryParam("Password")
        public String pass;

        @QueryParam("Address")
        public String address;

        @QueryParam("IDType")
        public String idtype;

        @QueryParam("IDNumber")
        public String idnum;

        @QueryParam("HomePhone")
        public String hphone;

    }


    @Path("/signup")
    @GET
    public Response signUp(@BeanParam ParameterBean paramBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.guest where Username = '" + paramBean.name +"'");
            while(resultSet.next()){
                a++;
                found = true;
            }
            if(found){
                r.setResponse("exist");
            }
            else{
                String st = "Regular";
                String zer = "0";
                statement.executeUpdate("INSERT INTO mydb.guest " + "VALUES (default , '" + st + "', '" + paramBean.fname +"' , '"  + paramBean.sname + "', '" + paramBean.email + "', '" + paramBean.idtype + "', '" + paramBean.idnum + "', '" + paramBean.address + "', '" + paramBean.mphone + "','" + paramBean.hphone + "',  '" + paramBean.name + "',  '" + paramBean.pass +"')");
                r.setResponse("success");
                setAccUsername(paramBean.name);
            }
        }




        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    public static class checkBean {
        @QueryParam("HotelName")
        public String hName;

        @QueryParam("FromDate")
        public String fromDate;

        @QueryParam("ToDate")
        public String toDate;

        @QueryParam("RoomType")
        public String roomType;

    }



    @Path("/check")
    @GET
    public Response check(@BeanParam checkBean checkBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        int booked = 0;
        int total = 0;
        int startWeek = 0;
        int endWeek = 0;
        int dateDiff = 0;
        int businessDays = 0;
        int weekends = 0;
        int price = 0;
        double totalPrice = 0.0;
        int reservationID = 0;
        int hotelIdNum = 0;
        int weekendPrice = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT datediff(least('" + checkBean.toDate + "', s.End_date), greatest('" + checkBean.fromDate + "', s.Start_date)) as dateDiff, s.AdditionalPrice FROM mydb.season s, mydb.hotel_has_season hs, mydb.hotel h  where h.Hotel_ID = hs.Hotel_Hotel_ID and hs.Season_Name = s.Name and h.Name = '" + checkBean.hName + "' and ( (s.Start_date >= '" + checkBean.fromDate + "' and s.Start_date < '" + checkBean.toDate + "') or (s.End_date <= '" + checkBean.toDate + "' and s.End_date > '" + checkBean.fromDate + "') or (s.Start_date < '" + checkBean.fromDate + "' and s.End_date >= '" + checkBean.toDate + "') );");
            while(resultSet.next()){
                a++;
                int p = resultSet.getInt("AdditionalPrice");
                int dd = resultSet.getInt("dateDiff");
                totalPrice += dd * p;
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + checkBean.fromDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                startWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + checkBean.toDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                endWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("select datediff('" + checkBean.toDate + "', '" + checkBean.fromDate +"') as dateDiff;");
            while(resultSet.next()){
                a++;
                dateDiff = resultSet.getInt("dateDiff");
            }

            while(dateDiff > 0){
                if(startWeek < 5){
                    businessDays++;
                    startWeek++;
                }
                else{
                    weekends++;
                    startWeek++;
                }
                startWeek = startWeek % 7;
                dateDiff--;
            }

            resultSet = statement.executeQuery("SELECT * from mydb.room_type r where r.Type = '" + checkBean.roomType + "' ;");
            while(resultSet.next()){
                a++;
                price = resultSet.getInt("Price");
                break;
            }
            weekendPrice = price + 10;
            totalPrice += businessDays * price + weekends * weekendPrice;

            resultSet = statement.executeQuery("SELECT count(*) as num FROM mydb.reservation r, hotel h, room_type_has_reservation t  where ( (r.from_date >= '" + checkBean.fromDate + "' and r.from_date < '" + checkBean.toDate + "') or (r.to_date <= '" + checkBean.toDate + "' and r.to_date > '" + checkBean.fromDate + "') or (r.from_date < '" + checkBean.fromDate + "' and r.to_date >= '" + checkBean.toDate + "') )and h.Name = '" + checkBean.hName + "' and r.Reservation_ID = t.Reservation_Reservation_ID and t.Room_Type_Hotel_Hotel_ID = h.Hotel_ID and t.Room_Type_Type = '" + checkBean.roomType + "';");
            while(resultSet.next()){
                a++;
                booked = resultSet.getInt("num");
            }
            resultSet = statement.executeQuery("select count(*) as num from room_type rt, hotel h where rt.Type = '" + checkBean.roomType + "' and rt.Hotel_Hotel_ID = h.Hotel_ID and h.Name = '" + checkBean.hName + "';");
            while(resultSet.next()){
                a++;
                total = resultSet.getInt("num");
            }

            if(!position.equals("User")){
                resultSet = statement.executeQuery("select gt.Discount from Guest g, Guest_type gt where g.Guest_ID = " + guestId +" and gt.Guest_type = g.Guest_type_Guest_type;");
                while(resultSet.next()){
                    a++;
                    double disc = resultSet.getDouble("Discount");
                    totalPrice *= disc;
                }
            }
        }

        CheckHelper r = new CheckHelper();

        if(total > booked){
            r.setResponse("yes");
            r.setPrice(totalPrice);
        }
        else{
            r.setResponse("no");
            r.setPrice(0);
        }



        json = gson.toJson(r, CheckHelper.class);

        return Response.ok(json).build();

    }

    public static class reserveBean {
        @QueryParam("HotelName")
        public String hName;

        @QueryParam("FromDate")
        public String fromDate;

        @QueryParam("ToDate")
        public String toDate;

        @QueryParam("RoomType")
        public String roomType;

        @QueryParam("Names[]")
        public List<String> oNames;

    }

    @Path("/reserve")
    @GET
    public Response reserve(@BeanParam reserveBean reserveBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        int booked = 0;
        int total = 0;
        int startWeek = 0;
        int endWeek = 0;
        int dateDiff = 0;
        int businessDays = 0;
        int weekends = 0;
        int price = 0;
        double totalPrice = 0.0;
        int reservationID = 0;
        int hotelIdNum = 0;
        int weekendPrice = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT datediff(least('" + reserveBean.toDate + "', s.End_date), greatest('" + reserveBean.fromDate + "', s.Start_date)) as dateDiff, s.AdditionalPrice FROM mydb.season s, mydb.hotel_has_season hs, mydb.hotel h  where h.Hotel_ID = hs.Hotel_Hotel_ID and hs.Season_Name = s.Name and h.Name = '" + reserveBean.hName + "' and ( (s.Start_date >= '" + reserveBean.fromDate + "' and s.Start_date < '" + reserveBean.toDate + "') or (s.End_date <= '" + reserveBean.toDate + "' and s.End_date > '" + reserveBean.fromDate + "') or (s.Start_date < '" + reserveBean.fromDate + "' and s.End_date >= '" + reserveBean.toDate + "') );");
            while(resultSet.next()){
                a++;
                int p = resultSet.getInt("AdditionalPrice");
                int dd = resultSet.getInt("dateDiff");
                totalPrice += dd * p;
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + reserveBean.fromDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                startWeek = resultSet.getInt("weekDay");
            }


            resultSet = statement.executeQuery("SELECT WEEKDAY('" + reserveBean.toDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                endWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("select datediff('" + reserveBean.toDate + "', '" + reserveBean.fromDate +"') as dateDiff;");
            while(resultSet.next()){
                a++;
                dateDiff = resultSet.getInt("dateDiff");
            }

            while(dateDiff > 0){
                if(startWeek < 5){
                    businessDays++;
                    startWeek++;
                }
                else{
                    weekends++;
                    startWeek++;
                }
                startWeek = startWeek % 7;
                dateDiff--;
            }

            resultSet = statement.executeQuery("SELECT * from mydb.room_type r where r.Type = '" + reserveBean.roomType + "' ;");
            while(resultSet.next()){
                a++;
                price = resultSet.getInt("Price");
                break;
            }
            weekendPrice = price + 10;
            totalPrice += businessDays * price + weekends * weekendPrice;

            if(!position.equals("User")){
                resultSet = statement.executeQuery("select gt.Discount from Guest g, Guest_type gt where g.Guest_ID = " + guestId +" and gt.Guest_type = g.Guest_type_Guest_type;");
                while(resultSet.next()){
                    a++;
                    double disc = resultSet.getDouble("Discount");
                    totalPrice *= disc;
                }
            }

            statement.executeUpdate("insert into mydb.reservation (Guest_Guest_ID, from_date, to_date, Price) values(" + guestId + ", '" + reserveBean.fromDate + "', '" + reserveBean.toDate +"', " + totalPrice + ");");

            resultSet = statement.executeQuery("SELECT count(*) as num, g.Guest_type_Guest_type from mydb.reservation r, guest g where r.Guest_Guest_ID = " + guestId +" and g.Guest_ID = " + guestId + ";");
            while(resultSet.next()){
                int num = resultSet.getInt("num");
                String status = resultSet.getString("Guest_type_Guest_type");
                if(num > 5 && num <= 20 && status.equals("Regular")){
                    statement.executeUpdate("update guest set Guest_type_Guest_type = 'Loyal' where Guest_ID = " + guestId + ";");
                }
                else if(num > 20 && status.equals("Loyal")){
                    statement.executeUpdate("update guest set Guest_type_Guest_type = 'VIP' where Guest_ID = " + guestId + ";");
                }
                break;
            }


//            if(true){
//                r.setResponse("reservation complete");
//
//
//                json = gson.toJson(r, ResponseHelper.class);
//
//                return Response.ok(json).build();
//            }


            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as num;");
            while(resultSet.next()){
                a++;
                reservationID = resultSet.getInt("num");
                break;
            }

            resultSet = statement.executeQuery("SELECT * from hotel h where h.Name = '" + reserveBean.hName + "';");
            while(resultSet.next()){
                a++;
                hotelIdNum = resultSet.getInt("Hotel_ID");
                break;
            }

            statement.executeUpdate("insert into mydb.room_type_has_reservation (Room_Type_Hotel_Hotel_ID, Room_Type_Type, Reservation_Reservation_ID, Reservation_Guest_Guest_ID) values(" + hotelIdNum + ", '" + reserveBean.roomType + "', " + reservationID +", " + guestId + ");");

            for(int i = 0; i < reserveBean.oNames.size(); i++){
                if(reserveBean.oNames.get(i).isEmpty()){
                    continue;
                }
                statement.executeUpdate("insert into mydb.occupants (Name, Reservation_Reservation_ID, Reservation_Guest_Guest_ID) values('" + reserveBean.oNames.get(i) + "', " + reservationID + ", " + guestId +");");
            }
        }
        r.setResponse("reservation complete");


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/getreservations")
    public Response getReservations() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.room_type_has_reservation t, mydb.reservation r where t.Room_Type_Hotel_Hotel_ID = " + getHotelId() + " and  t.Reservation_Reservation_ID = r.Reservation_ID;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(String.valueOf(resultSet.getInt("Reservation_ID")));
                res.add(String.valueOf(resultSet.getInt("Guest_Guest_ID")));
                res.add(resultSet.getString("from_date"));
                res.add(resultSet.getString("to_date"));
                res.add(String.valueOf(resultSet.getDouble("Price")));
                res.add(resultSet.getString("checkIn"));
                res.add(resultSet.getString("checkOut"));
                list.add(res);
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(list)).build();

    }

    @GET
    @Path("/getuserreservations")
    public Response getUserReservations() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.reservation r, hotel h, room_type_has_reservation rh where r.Guest_Guest_ID = " + guestId + " and r.Reservation_ID = rh.Reservation_Reservation_ID and rh.Room_Type_Hotel_Hotel_ID = h.Hotel_ID;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(resultSet.getString("Name"));
                res.add(String.valueOf(resultSet.getInt("Reservation_ID")));
                res.add(String.valueOf(resultSet.getInt("Guest_Guest_ID")));
                res.add(resultSet.getString("from_date"));
                String toDate = resultSet.getString("to_date");
                res.add(toDate);

                res.add(String.valueOf(resultSet.getInt("Price")));
//
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(cal.getTime());


                if(formatted.compareTo(toDate) < 0){
                    res.add("future");
                }
                else{
                    res.add("past");
                }

                res.add(resultSet.getString("checkIn"));
                res.add(resultSet.getString("checkOut"));
                list.add(res);

            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(list)).build();

    }

    @Path("/deletereservation")
    @GET
    public Response deleteReservation(@QueryParam("ReservationID") int reservationID) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            statement.executeUpdate("delete from mydb.occupants where Reservation_Reservation_ID = " + reservationID +";");
            statement.executeUpdate("delete from mydb.room_type_has_reservation where Reservation_Reservation_ID = " + reservationID +";");
            statement.executeUpdate("delete from mydb.reservation where Reservation_ID = " + reservationID +";");


        }



        r.setResponse("reservation deleted");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    public static class changeBean {
        @QueryParam("ReservationID")
        public int rID;

        @QueryParam("FromDate")
        public String fromDate;

        @QueryParam("ToDate")
        public String toDate;

        @QueryParam("Price")
        public String price;


    }

    @Path("/changereservation")
    @GET
    public Response changeReservation(@BeanParam changeBean changeBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            statement.executeUpdate("update reservation set from_date = '" + changeBean.fromDate + "', to_date = '" + changeBean.toDate + "', Price = " + changeBean.price + " where Reservation_ID = " + changeBean.rID + ";");

        }

        r.setResponse("reservation changed");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/getemployees")
    public Response getEmployees() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.employee e, mydb.schedule s where e.Hotel_Hotel_ID = " + hotelId + " and e.Supervisor is not null and e.EmployeeID = s.Employee_EmployeeID;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(resultSet.getString("Name"));
                res.add(String.valueOf(resultSet.getInt("EmployeeID")));
                res.add(resultSet.getString("week_start"));
                res.add(resultSet.getString("week_end"));
                res.add(resultSet.getString("working_day_start"));
                res.add(resultSet.getString("working_day_end"));
                res.add(String.valueOf(resultSet.getDouble("Salary")));
                list.add(res);
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(list)).build();

    }

    public static class employeeBean {
        @QueryParam("employeeID")
        public int eID;

        @QueryParam("weekStart")
        public String weekStart;

        @QueryParam("weekEnd")
        public String weekEnd;

        @QueryParam("dayStart")
        public String dayStart;

        @QueryParam("dayEnd")
        public String dayEnd;

        @QueryParam("Salary")
        public String salary;


    }

    @Path("/changeemployees")
    @GET
    public Response changeEmployees(@BeanParam employeeBean employeeBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            statement.executeUpdate("update schedule set week_start = '" + employeeBean.weekStart + "', week_end = '" + employeeBean.weekEnd + "', working_day_start = '" + employeeBean.dayStart + "', working_day_end = '" + employeeBean.dayEnd + "', Salary = " + employeeBean.salary + " where Employee_EmployeeID = " + employeeBean.eID + ";");
            statement.executeUpdate("update employee set Notification = 'Manager changed your schedule or salary' where EmployeeID = '" + employeeBean.eID + "';");
        }

        r.setResponse("employee schedule changed");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @Path("/notification")
    @GET
    public Response notification() throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from employee where EmployeeID = '" + employeeId +"';");
            while(resultSet.next()){
                String b = resultSet.getString("Notification");
                if(b == null){
                    r.setResponse("none");
                }
                else{
                    r.setResponse(b);
                }
            }
            statement.executeUpdate("update employee set Notification = null where EmployeeID = '" + employeeId + "';");

        }

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/getseasons")
    public Response getSeasons() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("select * from season s, hotel_has_season h where h.Hotel_Hotel_ID = " + hotelId +" and h.Season_Name = s.Name;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(resultSet.getString("Name"));
                res.add(resultSet.getString("Start_date"));
                res.add(resultSet.getString("End_date"));
                res.add(String.valueOf(resultSet.getDouble("AdditionalPrice")));
                list.add(res);
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(list)).build();

    }

    public static class seasonBean {
        @QueryParam("Name")
        public String Name;

        @QueryParam("startDate")
        public String startDate;

        @QueryParam("endDate")
        public String endDate;

        @QueryParam("addPrice")
        public int addPrice;

    }

    @Path("/changeseason")
    @GET
    public Response changeSeasons(@BeanParam seasonBean seasonBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            statement.executeUpdate("update season set Start_date = '" + seasonBean.startDate + "', End_date = '" + seasonBean.endDate + "',  AdditionalPrice = " + seasonBean.addPrice + " where Name = '" + seasonBean.Name + "';");

        }

        r.setResponse("season changed");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    public static class createSeasonBean {
        @QueryParam("name")
        public String Name;

        @QueryParam("fromDate")
        public String startDate;

        @QueryParam("toDate")
        public String endDate;

        @QueryParam("addPrice")
        public int addPrice;

    }

    @Path("/createseason")
    @GET
    public Response createSeasons(@BeanParam createSeasonBean createSeasonBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;

        int hID = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select count(*) as num from season where Name = '" + createSeasonBean.Name + "';");
            while(resultSet.next()){
                if(resultSet.getInt("num") > 0){
                    found = true;
                }
            }
            if(found){
                resultSet = statement.executeQuery("select count(*) as num from hotel_has_season where Hotel_Hotel_ID = " + hotelId + " and Season_Name = '" + createSeasonBean.Name + "';");
                while(resultSet.next()){
                    if(resultSet.getInt("num") > 0){
                        found = true;
                        r.setResponse("exists");
                        json = gson.toJson(r, ResponseHelper.class);

                        return Response.ok(json).build();
                    }
                }
                statement.executeUpdate("insert into hotel_has_season values(" + hotelId + ", '" + createSeasonBean.Name + "');");
            }
            else{
                statement.executeUpdate("insert into season values('" + createSeasonBean.Name + "', '" + createSeasonBean.startDate + "', '" + createSeasonBean.endDate + "', " + createSeasonBean.addPrice + ");");
                statement.executeUpdate("insert into hotel_has_season values(" + hotelId + ", '" + createSeasonBean.Name + "');");
            }


        }

        r.setResponse("season created");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @Path("/deleteseason")
    @GET
    public Response deleteSeason(@QueryParam("Name") String name) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            statement.executeUpdate("delete from hotel_has_season where Season_Name = '" + name + "';");

            statement.executeUpdate("delete from season where Name = '" + name + "';");

        }

        r.setResponse("season deleted");

        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }

    @GET
    @Path("/employeeinfo")
    public Response employeeInfo() throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<String> res = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("select * from mydb.employee e, mydb.schedule s where e.EmployeeID = '" + employeeId +"' and s.Employee_EmployeeID = e.EmployeeID;");
            while(resultSet.next()){
                a++;
                res.add("Guest ID: " + String.valueOf(resultSet.getInt("EmployeeID")));
                res.add("Hotel ID: " + String.valueOf(resultSet.getInt("Hotel_Hotel_ID")));
                res.add("Name: " + resultSet.getString("Name"));
                res.add("Position: " + resultSet.getString("Position"));
                res.add("Username: " + resultSet.getString("Username"));
                res.add("Supervisor: " + resultSet.getString("Supervisor"));

                res.add("Week start: " + resultSet.getString("week_start"));
                res.add("Week end: " + resultSet.getString("week_end"));
                res.add("Working day start: " + resultSet.getString("working_day_start"));
                res.add("Working day end: " + resultSet.getString("working_day_end"));
                res.add("Salary: " + resultSet.getString("Salary"));
            }
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }


        if(a == -1){
            r.setResponse("Something wrong happened");
            json = gson.toJson(r, ResponseHelper.class);

            return Response.ok(json).build();
        }

        return Response.ok(gson.toJson(res)).build();

    }

    @GET
    @Path("/showbill")
    public Response showBill(@QueryParam("rID") int rID) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            int k = 0;
            ResultSet resultSet = statement.executeQuery("select * from expenditure e, room_type_has_reservation rh, hotel h, reservation r where e.Reservation_Reservation_ID = " + rID + " and r.Reservation_ID = e.Reservation_Reservation_ID and rh.Reservation_Reservation_ID = r.Reservation_ID and rh.Room_Type_Hotel_Hotel_ID = h.Hotel_ID;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(String.valueOf(resultSet.getInt("Reservation_Guest_Guest_ID")));
                res.add(String.valueOf(resultSet.getInt("Reservation_Reservation_ID")));
                res.add(resultSet.getString("Name"));
                res.add(resultSet.getString("from_date"));
                res.add(resultSet.getString("to_date"));
                res.add(resultSet.getString("Type"));
                res.add(resultSet.getString("Date"));

                int t = resultSet.getInt("Price");
                res.add(String.valueOf(t));
                k += t;

                list.add(res);

            }

            List<String> res = new CopyOnWriteArrayList<>();
            res.add("-");
            res.add("-");
            res.add("-");
            res.add("-");
            res.add("-");
            res.add("-");
            res.add("-");
            res.add("Total: " + k);
            list.add(res);
            if(a == 0){
                a = -1;
            }
        }
        catch(Exception e){
            if(true){
            }
        }



        return Response.ok(gson.toJson(list)).build();

    }

    @GET
    @Path("/addservice")
    public Response addService(@QueryParam("rID") int rID, @QueryParam("serviceName") String serviceName) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            int k = 0;
            int pr = 0;
            ResultSet resultSet = statement.executeQuery("select * from room_type_has_reservation rh, hotel h, services s where rh.Reservation_Reservation_ID = " + rID + " and rh.Room_Type_Hotel_Hotel_ID = h.Hotel_ID and h.Hotel_ID = s.Hotel_ID and s.Name = '" + serviceName + "';");
            while(resultSet.next()){
                a++;
                pr = resultSet.getInt("Price");
            }
            statement.executeUpdate("insert into expenditure (Type, Date, Reservation_Reservation_ID, Reservation_Guest_Guest_ID, Price) values('" + serviceName + "', curdate(), " + rID + ", " + guestId + ", " + pr + ");");
            r.setResponse("service added");
        }
        catch(Exception e){
            if(true){
            }
        }


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();


    }

    @GET
    @Path("/checkservices")
    public Response addService(@QueryParam("rID") int rID) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
        List<List<String>> list = new CopyOnWriteArrayList<>();
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            int k = 0;
            int pr = 0;
            ResultSet resultSet = statement.executeQuery("select s.Name, s.Price from room_type_has_reservation rh, hotel h, services s where rh.Reservation_Reservation_ID = " + rID + " and rh.Room_Type_Hotel_Hotel_ID = h.Hotel_ID and h.Hotel_ID = s.Hotel_ID;");
            while(resultSet.next()){
                a++;
                List<String> res = new CopyOnWriteArrayList<>();
                res.add(resultSet.getString("Name"));
                res.add(String.valueOf(resultSet.getInt("Price")));
                list.add(res);
            }

        }
        catch(Exception e){
            if(true){
            }
        }


        return Response.ok(gson.toJson(list)).build();


    }




    @GET
    @Path("/checkin")
    public Response checkIn(@QueryParam("rID") int rID) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){


            statement.executeUpdate("update reservation set checkIn = 1 where Reservation_ID = " + rID +";");
            r.setResponse("check in completed.");
        }
        catch(Exception e){
            if(true){
            }
        }


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();


    }

    @GET
    @Path("/checkout")
    public Response checkOut(@QueryParam("rID") int rID) throws ClassNotFoundException, SQLException {


        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");


        int a = 0;
//Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){


            statement.executeUpdate("update reservation set checkIn = 1, checkOut = 1 where Reservation_ID = " + rID +";");
            r.setResponse("check out completed.");
        }
        catch(Exception e){
            if(true){
            }
        }


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();


    }


//    @POST
//    public Response postListItem(@FormParam("newEntry") String entry) {
//        list.add(0, entry);
//
//        return Response.ok().build();
//    }
//
//    @POST
//    @Path("/delete")
//    public Response deleteListItem(@FormParam("newDeletion") String s) {
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).equals(s)){
//                list.remove(i);
//                break;
//            }
//        }
//        return Response.ok("deleted").build();
//    }
//
//    @GET
//    @Path("/deleteall")
//    public Response deleteAllListItem() {
//        list.clear();
//        Gson gson = new Gson();
//
//        return Response.ok(gson.toJson(list)).build();
//    }
public static class creserveBean {

    @QueryParam("GuestID")
    public String guestID;

    @QueryParam("FromDate")
    public String fromDate;

    @QueryParam("ToDate")
    public String toDate;

    @QueryParam("RoomType")
    public String roomType;

    @QueryParam("Names[]")
    public List<String> oNames;

}

    @Path("/createreservation")
    @GET
    public Response createReservation(@BeanParam creserveBean reserveBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;
        ResponseHelper r = new ResponseHelper();

        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        int booked = 0;
        int total = 0;
        int startWeek = 0;
        int endWeek = 0;
        int dateDiff = 0;
        int businessDays = 0;
        int weekends = 0;
        int price = 0;
        double totalPrice = 0.0;
        int reservationID = 0;
        int hotelIdNum = 0;
        int weekendPrice = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT datediff(least('" + reserveBean.toDate + "', s.End_date), greatest('" + reserveBean.fromDate + "', s.Start_date)) as dateDiff, s.AdditionalPrice FROM mydb.season s, mydb.hotel_has_season hs, mydb.hotel h  where h.Hotel_ID = hs.Hotel_Hotel_ID and hs.Season_Name = s.Name and h.Hotel_ID = '" + hotelId + "' and ( (s.Start_date >= '" + reserveBean.fromDate + "' and s.Start_date < '" + reserveBean.toDate + "') or (s.End_date <= '" + reserveBean.toDate + "' and s.End_date > '" + reserveBean.fromDate + "') or (s.Start_date < '" + reserveBean.fromDate + "' and s.End_date >= '" + reserveBean.toDate + "') );");
            while(resultSet.next()){
                a++;
                int p = resultSet.getInt("AdditionalPrice");
                int dd = resultSet.getInt("dateDiff");
                totalPrice += dd * p;
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + reserveBean.fromDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                startWeek = resultSet.getInt("weekDay");
            }


            resultSet = statement.executeQuery("SELECT WEEKDAY('" + reserveBean.toDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                endWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("select datediff('" + reserveBean.toDate + "', '" + reserveBean.fromDate +"') as dateDiff;");
            while(resultSet.next()){
                a++;
                dateDiff = resultSet.getInt("dateDiff");
            }

            while(dateDiff > 0){
                if(startWeek < 5){
                    businessDays++;
                    startWeek++;
                }
                else{
                    weekends++;
                    startWeek++;
                }
                startWeek = startWeek % 7;
                dateDiff--;
            }

            resultSet = statement.executeQuery("SELECT * from mydb.room_type r where r.Type = '" + reserveBean.roomType + "' ;");
            while(resultSet.next()){
                a++;
                price = resultSet.getInt("Price");
                break;
            }
            weekendPrice = price + 10;
            totalPrice += businessDays * price + weekends * weekendPrice;

            if(!position.equals("User")){
                resultSet = statement.executeQuery("select gt.Discount from Guest g, Guest_type gt where g.Guest_ID = " + reserveBean.fromDate +" and gt.Guest_type = g.Guest_type_Guest_type;");
                while(resultSet.next()){
                    a++;
                    double disc = resultSet.getDouble("Discount");
                    totalPrice *= disc;
                }
            }

            statement.executeUpdate("insert into mydb.reservation (Guest_Guest_ID, from_date, to_date, Price) values(" + reserveBean.guestID + ", '" + reserveBean.fromDate + "', '" + reserveBean.toDate +"', " + totalPrice + ");");

            resultSet = statement.executeQuery("SELECT count(*) as num, g.Guest_type_Guest_type from mydb.reservation r, guest g where r.Guest_Guest_ID = " + reserveBean.guestID +" and g.Guest_ID = " + reserveBean.guestID + ";");
            while(resultSet.next()){
                int num = resultSet.getInt("num");
                String status = resultSet.getString("Guest_type_Guest_type");
                if(num > 5 && num <= 20 && status.equals("Regular")){
                    statement.executeUpdate("update guest set Guest_type_Guest_type = 'Loyal' where Guest_ID = " + reserveBean.guestID + ";");
                }
                else if(num > 20 && status.equals("Loyal")){
                    statement.executeUpdate("update guest set Guest_type_Guest_type = 'VIP' where Guest_ID = " + reserveBean.guestID + ";");
                }
                break;
            }


//            if(true){
//                r.setResponse("reservation complete");
//
//
//                json = gson.toJson(r, ResponseHelper.class);
//
//                return Response.ok(json).build();
//            }


            resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as num;");
            while(resultSet.next()){
                a++;
                reservationID = resultSet.getInt("num");
                break;
            }

            resultSet = statement.executeQuery("SELECT * from hotel h where h.Hotel_ID = '" + hotelId + "';");
            while(resultSet.next()){
                a++;
                hotelIdNum = resultSet.getInt("Hotel_ID");
                break;
            }

            statement.executeUpdate("insert into mydb.room_type_has_reservation (Room_Type_Hotel_Hotel_ID, Room_Type_Type, Reservation_Reservation_ID, Reservation_Guest_Guest_ID) values(" + hotelId + ", '" + reserveBean.roomType + "', " + reservationID +", " + reserveBean.guestID + ");");

            for(int i = 0; i < reserveBean.oNames.size(); i++){
                if(reserveBean.oNames.get(i).isEmpty()){
                    continue;
                }
                statement.executeUpdate("insert into mydb.occupants (Name, Reservation_Reservation_ID, Reservation_Guest_Guest_ID) values('" + reserveBean.oNames.get(i) + "', " + reservationID + ", " + reserveBean.guestID +");");
            }
        }
        r.setResponse("reservation complete");


        json = gson.toJson(r, ResponseHelper.class);

        return Response.ok(json).build();

    }
    public static class ccheckBean {

        @QueryParam("GuestID")
        public int guestID;

        @QueryParam("FromDate")
        public String fromDate;

        @QueryParam("ToDate")
        public String toDate;

        @QueryParam("RoomType")
        public String roomType;

    }



    @Path("/checkclerk")
    @GET
    public Response check(@BeanParam ccheckBean checkBean) throws ClassNotFoundException, SQLException {

        boolean found = false;

        Gson gson = new Gson();
        String json;


        String username = "root";
        String password = "root";

        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?autoReconnect=true&useSSL=false";

        Class.forName("com.mysql.jdbc.Driver");
        int a = 0;
        int booked = 0;
        int total = 0;
        int startWeek = 0;
        int endWeek = 0;
        int dateDiff = 0;
        int businessDays = 0;
        int weekends = 0;
        int price = 0;
        double totalPrice = 0.0;
        int reservationID = 0;
        int hotelIdNum = 0;
        int weekendPrice = 0;
        try(Connection connection = DriverManager.getConnection(connectionUrl, username, password); Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery("SELECT datediff(least('" + checkBean.toDate + "', s.End_date), greatest('" + checkBean.fromDate + "', s.Start_date)) as dateDiff, s.AdditionalPrice FROM mydb.season s, mydb.hotel_has_season hs, mydb.hotel h  where h.Hotel_ID = hs.Hotel_Hotel_ID and hs.Season_Name = s.Name and h.Hotel_ID = '" + hotelId + "' and ( (s.Start_date >= '" + checkBean.fromDate + "' and s.Start_date < '" + checkBean.toDate + "') or (s.End_date <= '" + checkBean.toDate + "' and s.End_date > '" + checkBean.fromDate + "') or (s.Start_date < '" + checkBean.fromDate + "' and s.End_date >= '" + checkBean.toDate + "') );");
            while(resultSet.next()){
                a++;
                int p = resultSet.getInt("AdditionalPrice");
                int dd = resultSet.getInt("dateDiff");
                totalPrice += dd * p;
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + checkBean.fromDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                startWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("SELECT WEEKDAY('" + checkBean.toDate +"') as weekDay;");
            while(resultSet.next()){
                a++;
                endWeek = resultSet.getInt("weekDay");
            }

            resultSet = statement.executeQuery("select datediff('" + checkBean.toDate + "', '" + checkBean.fromDate +"') as dateDiff;");
            while(resultSet.next()){
                a++;
                dateDiff = resultSet.getInt("dateDiff");
            }

            while(dateDiff > 0){
                if(startWeek < 5){
                    businessDays++;
                    startWeek++;
                }
                else{
                    weekends++;
                    startWeek++;
                }
                startWeek = startWeek % 7;
                dateDiff--;
            }

            resultSet = statement.executeQuery("SELECT * from mydb.room_type r where r.Type = '" + checkBean.roomType + "' ;");
            while(resultSet.next()){
                a++;
                price = resultSet.getInt("Price");
                break;
            }
            weekendPrice = price + 10;
            totalPrice += businessDays * price + weekends * weekendPrice;

            resultSet = statement.executeQuery("SELECT count(*) as num FROM mydb.reservation r, hotel h, room_type_has_reservation t  where ( (r.from_date >= '" + checkBean.fromDate + "' and r.from_date < '" + checkBean.toDate + "') or (r.to_date <= '" + checkBean.toDate + "' and r.to_date > '" + checkBean.fromDate + "') or (r.from_date < '" + checkBean.fromDate + "' and r.to_date >= '" + checkBean.toDate + "') )and h.Hotel_ID = '" + hotelId + "' and r.Reservation_ID = t.Reservation_Reservation_ID and t.Room_Type_Hotel_Hotel_ID = h.Hotel_ID and t.Room_Type_Type = '" + checkBean.roomType + "';");
            while(resultSet.next()){
                a++;
                booked = resultSet.getInt("num");
            }
            resultSet = statement.executeQuery("select count(*) as num from room_type rt, hotel h where rt.Type = '" + checkBean.roomType + "' and rt.Hotel_Hotel_ID = h.Hotel_ID and h.Hotel_ID = '" + hotelId + "';");
            while(resultSet.next()){
                a++;
                total = resultSet.getInt("num");
            }

            if(!position.equals("User")){
                resultSet = statement.executeQuery("select gt.Discount from Guest g, Guest_type gt where g.Guest_ID = " + checkBean.guestID +" and gt.Guest_type = g.Guest_type_Guest_type;");
                while(resultSet.next()){
                    a++;
                    double disc = resultSet.getDouble("Discount");
                    totalPrice *= disc;
                }
            }
        }

        CheckHelper r = new CheckHelper();

        if(total > booked){
            r.setResponse("yes");
            r.setPrice(totalPrice);
        }
        else{
            r.setResponse("no");
            r.setPrice(0);
        }



        json = gson.toJson(r, CheckHelper.class);

        return Response.ok(json).build();

    }
}
