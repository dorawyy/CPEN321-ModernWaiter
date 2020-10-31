const express = require('express')
const mysql = require('mysql')

module.exports = function(app, con){
    app.use(express.json());
    
    /**
     * HTTP GET request to get details of the table
     * a user is seated at. It returns the 
     * information with a status code of 200 
     * if successful.
     */
    app.get("/tables/:id", (req,res) => {    
        console.log("/tables/{{id}}");
        let id = req.params.id;
        let sql_query = mysql.format("SELECT * FROM tables WHERE id = ?", [id]);
        con.query(sql_query, function(err, result){
            if (err) {
                res.send(err);
            }
            res.send(result);
        });
    });
}