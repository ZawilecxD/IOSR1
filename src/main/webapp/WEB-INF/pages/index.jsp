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
    <script src="resources/jquery-3.1.1.min.js" type="text/javascript"></script>
    <style>
      .logsParent {
        width:32%;
        display:inline-block;
        vertical-align: top;
      }

      #getLogs thead, #postLogs thead {
        text-align: center;
      }

      #postSettings, #getSettings, #deleteSettings {
        border: 1px solid black;
        width:32%;
        min-height:220px;
        vertical-align: top;
        display: inline-block;
        text-align:center;
      }

      table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
      }

      td, th {
        border: 1px solid #dddddd;
        text-align: left;
        padding: 8px;
      }

      tr:nth-child(even) {
        background-color: #dddddd;
      }

    </style>
  </head>
  <body>
  <p>Podaj czas miedzy kolejnymi requestami w sekundach:</p>
  <div id="getSettings">
    <h3>GET</h3>
    <p>
      <input type="number" id="GETIntervalValue" value="3"/>
      <button id="setGETInterval">Set Interval</button>
      <button id="stopGETInterval">Stop Interval</button>
    </p>
    <p>
      <input type="text" id="getFileName" value="test1">
      <button id="getFileNameChange">Change FileName</button>
    </p>
    <p>
      <button id="getNOW">GET NOW with current parameters</button>
    </p>
  </div>
  <div id="postSettings">
    <h3>POST</h3>
    <p>
      <input type="number" id="POSTIntervalValue" value="3"/>
      <button id="setPOSTInterval">Set Interval</button>
      <button id="stopPOSTInterval">Stop Interval</button>
    </p>
    <p>
      <input type="text" id="postFileName" value="test2">
      <button id="postFileNameChange">Change FileName</button>
    </p>
    <p>
      <input type="text" id="postText" value="post random text">
      <button id="postTextChange">Change Text</button>
    </p>
    <p>
      <button id="postNOW">POST NOW with current parameters</button>
    </p>
  </div>
  <div id="deleteSettings">
    <h3>DELETE</h3>
    <p>
      <input type="number" id="DELETEIntervalValue" value="3"/>
      <button id="setDELETEInterval">Set Interval</button>
      <button id="stopDELETEInterval">Stop Interval</button>
    </p>
    <p>
      <input type="text" id="deleteFileName" value="test2">
      <button id="deleteFileNameChange">Change FileName</button>
    </p>
    <p>
      <button id="deleteNOW">DELETE NOW with current parameters</button>
    </p>
  </div>

  <div>
    <div class="logsParent">
      <table id="getLogs">
        <thead>GET LOGS</thead>
        <tr>
          <th>FileName</th>
          <th>Read Text</th>
          <th>Date</th>
          <th>Request status</th>
        </tr>
      </table>
    </div>
    <div class="logsParent">
      <table id="postLogs">
        <thead>POST LOGS</thead>
        <tr>
          <th>FileName</th>
          <th>Sent Text</th>
          <th>Date</th>
          <th>Request status</th>
        </tr>
      </table>
    </div>
    <div class="logsParent">
      <table id="deleteLogs">
        <thead>DELETE LOGS</thead>
        <tr>
          <th>FileName</th>
          <th>Date</th>
          <th>Request status</th>
        </tr>
      </table>
    </div>

  </div>


  <script>
    var getIntervalHandle = null;
    var postIntervalHandle = null;
    var deleteIntervalHandle = null;


    var getCurrentFileName = 'test1';
    var postCurrentFileName = 'test2';
    var deleteCurrentFileName = 'test2';
    var postCurrentText = 'random post text';

    function appendGetLog(fileName, readText, status) {
      $("#getLogs").append("<tr>" +
                          "<td>"+fileName+"</td>" +
                          "<td>"+readText+"</td>" +
                          "<td>"+getDateString()+"</td>" +
                          "<td>"+status+"</td>" +
                          "</tr>")
    }

    function appendPostLog(fileName, sentText, status) {
      $("#postLogs").append("<tr>" +
              "<td>"+fileName+"</td>" +
              "<td>"+sentText+"</td>" +
              "<td>"+getDateString()+"</td>" +
              "<td>"+status+"</td>" +
              "</tr>")
    }

    function appendDeleteLog(fileName, status) {
      $("#deleteLogs").append("<tr>" +
              "<td>"+fileName+"</td>" +
              "<td>"+getDateString()+"</td>" +
              "<td>"+status+"</td>" +
              "</tr>")
    }

    function getDateString() {
      var date = new Date();
      return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }

    function sendGetRequest() {
      var currentFileName = getCurrentFileName;
      $.ajax({
        type: "GET",
        url: window.location.href+"read/"+getCurrentFileName,
        success: function(data, textStatus, jqXHR ) {
          appendGetLog(currentFileName, data, textStatus);
        }
      });
    }

    function sendDeleteRequest() {
      var currentFileName = deleteCurrentFileName;
      $.ajax({
        type: "GET",
        url: window.location.href+"delete/"+getCurrentFileName,
        success: function(data, textStatus, jqXHR ) {
          appendDeleteLog(currentFileName, textStatus);
        }
      });
    }

    function sendPostRequest() {
      var currentFileName = postCurrentFileName;
      var currentText = postCurrentText;
      $.ajax({
        type: "POST",
        url: window.location.href+"write?fileName="+postCurrentFileName+"&text="+postCurrentText,
        success: function(data, textStatus, jqXHR  ) {
          appendPostLog(currentFileName, currentText, textStatus);
        },
      });
    }

    $(function() {
      $("#setGETInterval").on("click", function() {
        clearInterval(getIntervalHandle);
        getIntervalHandle = setInterval(sendGetRequest, $("#GETIntervalValue").val() * 1000);
      });

      $("#setPOSTInterval").on("click", function() {
        clearInterval(postIntervalHandle);
        postIntervalHandle = setInterval(sendPostRequest, $("#POSTIntervalValue").val() * 1000);
      });

      $("#setDELETEInterval").on("click", function() {
        clearInterval(deleteIntervalHandle);
        deleteIntervalHandle = setInterval(sendDeleteRequest, $("#DELETEIntervalValue").val() * 1000);
      });

      $("#getNOW").on("click", function() {
        sendGetRequest();
      });

      $("#postNOW").on("click", function() {
        sendPostRequest();
      });

      $("#deleteNOW").on("click", function() {
        sendDeleteRequest();
      });

      $("#stopGETInterval").on("click" , function() {
        clearInterval(getIntervalHandle);
      });

      $("#stopPOSTInterval").on("click" , function() {
        clearInterval(postIntervalHandle);
      });

      $("#stopDELETEInterval").on("click" , function() {
        clearInterval(deleteIntervalHandle);
      });

      $("#getFileNameChange").on("click" , function() {
        getCurrentFileName = $("#getFileName").val();
      });

      $("#postFileNameChange").on("click" , function() {
        postCurrentFileName = $("#postFileName").val();
      });

      $("#deleteFileNameChange").on("click" , function() {
        deleteCurrentFileName = $("#deleteFileName").val();
      });

      $("#postTextChange").on("click" , function() {
        postCurrentText = $("#postText").val();
      });

    })
  </script>
  </body>
</html>
