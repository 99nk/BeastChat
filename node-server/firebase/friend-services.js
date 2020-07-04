var app=require('express')();
var http=require('http').Server(app);
var io=require('socket.io')(http);//connecting client to the server


var admin = require("firebase-admin");

var userFriendsRequests=(io)=>
{
    io.on('connection',(socket)=>{
        console.log(`Client ${socket.id} has connected to friend services!` );
        sendOrDeleteFriendRequest(socket,io);
        detectDisconnection(socket,io);
        
    });
};

function sendOrDeleteFriendRequest(socket,io){
    socket.on('friendRequest',(data)=>
    {
        var friendEmail=data.email;
        var userEmail=data.userEmail;
        var requestCode=data.requestCode;

        var db=admin.database();
        var friendRef=db.ref('friendRequestRecieved').child(encodeEmail(friendEmail))
        .child(encodeEmail(userEmail));
        if(requestCode==0)
        {
            var db=admin.database();
            var ref=db.ref('users');
            var userRef=ref.child(encodeEmail(data.userEmail));

            userRef.once('value',(snapshot)=>
            {
                friendRef.set({
                   // snapshot 
                   email:snapshot.val().email,
                   username:snapshot.val().username,
                   userPicture:snapshot.val().userPicture,
                   dateJoined:snapshot.val().dateJoined,
                   hasLoggedIn:snapshot.val().hasLoggedIn          
                 });
            });
        }
        else
        {
            friendRef.remove();
        }
    });
}


function detectDisconnection(socket,io)
{
    socket.on('disconnect',()=>
    {
    console.log('A client has disconnected from friend services');
    
    });
}

function encodeEmail(email)
{
    return email.replace('.',',');
}

module.exports={
    userFriendsRequests
}