// Node.js
// $ npm install neo4j-driver
// $ node getdata.js
var neo4j = require('neo4j-driver').v1;
var driver = neo4j.driver("bolt://127.0.0.1", neo4j.auth.basic("neo4j", "12345678"));
var session = driver.session();
session
  .run( "MATCH (u:User) WHERE u.name = 'user' RETURN u" )
  .then(function( result ) {
  	var record = result.records[0];
  	var user = record['_fields'];
  	console.log(JSON.stringify(user, null, 4));
    session.close();
    driver.close();
  }, function (err) {
  console.error(err);
})
  
  