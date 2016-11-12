<%--
  Created by IntelliJ IDEA.
  User: Murzynas
  Date: 2016-11-05
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Manager</title>
    <script src="../jquery-3.1.1.min.js"></script>
  </head>
  <body>
  <p>Podaj czas miedzy kolejnymi requestami w sekundach:</p>
  <input type="number" id="timeoutValue"/>
  <button id="setTimeout">Start</button>

  <div id="getLogs">
    <h2>GET LOGS</h2>
  </div>
  <div id="postLogs">
    <h2>POST LOGS</h2>
  </div>


  <script>
    var timeoutHandle = null;
    var counter = 0;
    function appendGetLog(text) {
      $("#getLogs").append("<p>"+text+"</p>")
    }

    function appendPostLog() {

    }

    function sendGetRequest() {
      $.get("demo_test.asp", function(data, status){
        alert("Data: " + data + "\nStatus: " + status);
      });
    }

    function sendPostRequest() {
      $.post("demo_test_post.asp",
              {
                name: "Donald Duck",
                city: "Duckburg"
              },
              function(data, status){
                alert("Data: " + data + "\nStatus: " + status);
              });
    }

    $(function() {
      $("#setTimeout").on("click", function() {
        clearTimeout(timeoutHandle);
        timeoutHandle = setTimeout(sendGetRequest, $("#timeoutValue").val() * 1000);
      });
    })
  </script>
  </body>
</html>
