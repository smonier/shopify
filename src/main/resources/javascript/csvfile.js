
function csvToJson(csvString) {
    const lines = csvString.replace(/\r\n/g, '\n').replace(/\r/g, '\n').split('\n');

    // Extract headers, removing quotes
    const headers = lines[0].split(',').map(header => header.replace(/"/g, ''));

    // Function to remove HTML tags
    const removeHtmlTags = (str) => {
        return str.replace(/<\/?[^>]+(>|$)/g, "");
    };

    // Map the rows to objects
    const result = lines.slice(1).map(line => {
        const data = line.split(',').map(field => removeHtmlTags(field.replace(/"/g, '')));
        return headers.reduce((obj, nextKey, index) => {
            obj[nextKey] = data[index];
            return obj;
        }, {});
    });

    return result;
}

$(".csvTable").each(function () {
    var currentID = "#" + $(this).attr("id");
    $.ajax({
        url: $(this).data("url"),
        type: 'GET',
        dataType: 'text',
        success: function (csvData) {
            var jsonFromCsv = csvToJson(csvData);
            var table = new Tabulator(currentID, {
                data: jsonFromCsv, // provide the fetched CSV data
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
