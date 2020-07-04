var app=require('express')();
var http=require('http').Server(app);
var io=require('socket.io')(http);//connecting client to the server


var admin = require("firebase-admin");
var firebaseCredential=require(__dirname+'/private/serviceCredentials.json')



admin.initializeApp({
  credential: admin.credential.cert(firebaseCredential),
  databaseURL: "https://beastchat-d85c5.firebaseio.com"
});


var accountRequests=require('./firebase/account-services');
var friendReuests=require('./firebase/friend-services');

accountRequests.userAccountsRequests(io);
friendReuests.userFriendsRequests(io);


http.listen(3000,()=>{
    console.log('Server is listening at port 3000');
    
})