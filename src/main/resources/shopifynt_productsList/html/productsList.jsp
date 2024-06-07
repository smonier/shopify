<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%--@elvariable id="currentNode"
type="org.jahia.services.content.JCRNodeWrapper" --%>
<%--@elvariable id="out" type="java.io.PrintWriter" --%>
<%--@elvariable id="script"
type="org.jahia.services.render.scripting.Script" --%>
<%--@elvariable id="scriptInfo" type="java.lang.String" --%>
<%--@elvariable id="workspace" type="java.lang.String" --%>
<%--@elvariable id="renderContext"
type="org.jahia.services.render.RenderContext" --%>
<%--@elvariable id="currentResource"
type="org.jahia.services.render.Resource" --%>
<%--@elvariable id="url"
type="org.jahia.services.render.URLGenerator"
--%>

<template:addResources
    resources="tabulator.min.css"
    type="css" />
<template:addResources
    resources="tabulator.min.js"
    type="javascript" />
<template:addResources
    resources="csvfile.js"
    type="javascript" />
<template:addResources
    resources="tabulator_bootstrap5.css"
    type="css" />


<c:set var="title"
    value="${currentNode.properties['jcr:title'].string}" />
<c:set var="csvFile"
    value="${currentNode.properties['csvFile'].node}" />



<h2>${title}</h2>
<p>&nbsp;</p>
<c:if test="${not empty csvFile}">
    <div id="myCsv-${currentNode.identifier}"
        class="csvTable"
        data-url="${csvFile.getUrl()}">
    </div>
</c:if>

<script>

function replaceSemicolons(inputString) {
    return inputString.replace(/;/g, ',');
}

$(".csvTable").each(function () {
    var currentID = "#" + $(this).attr("id");
    $.ajax({
        url: $(this).data("url"),
        type: 'GET',
        dataType: 'text',
        success: function (csvData) {
            var table = new Tabulator(currentID, {
                data: replaceSemicolons(csvData), // provide the fetched CSV data
                importFormat:"csv",
                layout: "fitColumns", // Fit columns to width of table
                pagination: "local", // Enable local pagination
                paginationSize: 10, // Set number of rows per page
                placeholder: "No Data Available", // Display message when no data is available
                responsiveLayout: "collapse",
                groupBy:["id"],
                paginationCounter: "rows",
                columns: [
                    { formatter:"responsiveCollapse", width:30, minWidth:30, hozAlign:"center", resizable:false, headerSort:false},
                    { title: "id", field: "id" , responsive:0, headerFilter:"input"}, //column has a fixed width of 100px;
                    { title: "title", field: "title" }, //column has a fixed width of 100px;
                    { title: "body_html", field: "body_html" }, //column has a fixed width of 100px;
                    { title: "vendor", field: "vendor" }, //column has a fixed width of 100px;
                    { title: "product_type", field: "product_type" }, //column has a fixed width of 100px;
                    { title: "price", field: "price" }, //column has a fixed width of 100px;
                    { title: "product_type", field: "product_type" }, //column has a fixed width of 100px;
                    { title: "action", field: "action" }, //column has a fixed width of 100px;
                ],   
            });
        },
        error: function (xhr, status, error) {
            console.error("Error fetching CSV: " + status + " - " + error);
        }
    });
});

</script>