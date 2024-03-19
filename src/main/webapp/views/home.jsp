<%@page language="java" %>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="style.css">
        <style>
        body, h2, div {
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
        }

        .container {
            max-width: 800px;
            margin: auto;
            padding: 20px;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        .button-container {
            display: flex;
            justify-content: center;
        }

        .button-container a {
            text-decoration: none;
        }

        .button {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 15px 30px;
            border: none;
            text-align: center;
            text-decoration: none;
            font-size: 16px;
            margin: 10px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .button:hover {
            background-color: #0056b3;
        }

        .button:active {
            background-color: #004b9e;
        }

        </style>
    </head>
    </body>
        <h2>Enter Admin Details</h2>


         <div class="display1employee" style="max-width:500px; margin:auto;">

             <a href="/adminLogin">
             <input type="submit" value="Admin" style="height:60px;">
             </a>
             <a href="UserLogin">
              <input type="submit" value="User" style="height:60px;">
              </a>


         </div>
         </body>
  </html>