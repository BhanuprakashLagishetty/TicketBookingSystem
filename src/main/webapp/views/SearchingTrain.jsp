<%@page language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<a href="javascript:history.go(-1)" style="position:absolute; top:10px; left:30px">Go Back</a>
<a href="HistoryOfBooking">Click here for History OF Booking</a>
<a href="showBankBalance">check Bank Balance</a>
<h2>Enter Starting and ending points</h2>


<form:form action="findTrains" modelAttribute="SearchTrainModel">
    <label for="arrivalStation">Enter Starting Station:</label>
    <input type="text" id="arrivalStation" name="arrivalStation">
    <form:errors path="arrivalStation" cssClass="error"/><br>
        <label for="departureStation">Enter Destination Station:</label>
        <input type="text" id="departureStation" name="departureStation">
        <form:errors path="departureStation" cssClass="error"/><br>
        <label for="bookingDate">Booking Date:</label>
        <input type="date" id="bookingDate" name="bookingDate">
        <form:errors path="bookingDate" cssClass="error"/><br>
        <input type="submit" value="Submit">

</form:form>

</body>
</html>