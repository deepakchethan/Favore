var mongoose = require('mongoose');
var passport = require('passport');
var config = require('../config/database');
require('../config/passport')(passport);
var express = require('express');
var jwt = require('jsonwebtoken');
var router = express.Router();
var User = require("../models/user");
var Post = require('../models/post');

router.post('/postimg/',function(req,res,next){
  // Images available at imagepath/postId.jpg
  var img = req.files.file;
  if (!req.files){
    res.json({success:false,msg:"No image to upload bch!"});
  }
  else {
  var path = '/images/'
  path.concat(req.body.posterId);
  path.concat(JSON.stringify(req.body.location));
  path.concat(JSON.stringify(req.body.date));
  path.concat(".jpg");
  file.mv(path,function(err){
    if (err){
      res.json({success:false,msg:"Failed to upload the file"});
    }
    res.json({success:true});
  });
  var truePath = "https://172.0.0.1:3000"
  truePath.concat(path);
  var newPost = new Post({
    postText:req.body.postText,
    location:req.body.location,
    favors:req.body.favors,
    age:req.body.age,
    date:req.body.date,
    posterId:req.body.posterId,
    isImage:true,
    imagePath: truePath
  })
  newPost.save(function(err,suc){
    if (err){
      res.json({success:false});
    }
    else{
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
            res.json({success:false,msg:"Some kind of error"});
          } if (usr == null){
            res.json({success:false,msg:"User not found"})
          } else{
            var usr_posts = usr.posts;
            usr_posts.push(suc.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err){
                  res.json({success:false,msg:"Some kind of error"});
              }
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            })
          }
        });
    }
  });
}
});


router.post('/posttext/',function(req,res,next){
  // Sending the post
  var newPost = new Post({
    postText:req.body.postText,
    location:req.body.location,
    favors:req.body.favors,
    age:req.body.age,
    date:req.body.date,
    posterId:req.body.posterId,
    isImage:false
  });
  newPost.save(function(err,suc){
    if (err){
      res.json({success:false});
    }
    else{
        User.findOne({id:req.body.posterId},function(err,usr){
          if (err){
            res.json({success:false,msg:"Some kind of error"});
          } if (usr == null){
            res.json({success:false,msg:"User not found"})
          } else{
            var usr_posts = usr.posts;
            usr_posts.push(suc.id);
            User.update({id:usr.id},{$set:{posts:usr_posts}},function(err,out){
              if (err){
                  res.json({success:false,msg:"Some kind of error"});
              }
              else res.json({success:true, msg:"Successfully inserted a post mate!", post:suc});
            })
          }
        });
    }
  });
});

router.get('/fetchPosts/',function(req,res,next) {
  /*
  usage: localhost:3000/user/fetchPosts?lon=xx&lat=xx
  */
  Post.aggregate(
        [
            {
                '$geoNear': {
                    'near': [parseFloat(req.query.lon),parseFloat(req.query.lat)],
                    'spherical': true,
                    'distanceField': 'dist',
                    'maxDistance': 1000
                }
            }
        ],
        function(err, results) {
            // do what you want with the results here
            if (err){
              res.json({"msg":"ding dong bell","err":err});
            }
            var posts = [];
            for (var i = 0; i < results.length; ++i){
              var temp = results[i];
              var post = new Post({
                postText:temp.postText,
                location:temp.location,
                favors:temp.favors,
                id:temp.id,
                age:temp.age,
                date:temp.date
              });
              post.computeAge();
              posts.push(post);
            }
            res.json({success:"true",posts:posts})
        }
    )
});

router.get('/getUserDetails/:user_id',function(req,res,next) {
  /*
  usage: localhost:3000/<usr-name>
  */
  var user_id = req.params.user_id;
  User.find({id:user_id},function(err,usr){
    if (err){
      req.json({success:false,message: "Some kind of error!"});
    }
    if (usr == null){
        res.json({success:false,msg:"No such user"});
    }
    else res.json({success:true,user:usr});
  });
})

router.get('/getAllUsers',function(req,res,next){
  /*
    usage:localhost:3000/
    Returns all the uses in the collection
  */
  User.find(function(err,usrs){
    if (err) throw err;
    else res.json({"usr":usrs});
  })
});

router.get('/removePost/:post_id',function(req,res,next){
  /*
    usage:localhost:3000/<post+id>
    Removes the post and returns the ack
  */
  var post_id = req.params.post_id;
  Post.findOne({id:post_id},function(err,post){
    if (err){
      res.json({success:false,msg:"Some kind of error!"});
    }
    else if (post==null){
      res.json({success:false,msg:"No such post"});
    }
    else{
      var usr_id = post.posterId;
      User.findOne({id:usr_id},function(err,user){
        if (err){
          res.json({success:false,msg:"Some kind of error!"});
        }
        else if (post==null){
          res.json({success:false,msg:"No such user"});
        }
        else{
          var post_list = user.posts;
          post_list.splice(post_list.indexOf(post_id),1);
          User.update({id:user.id},{$set:{posts:post_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error!"});
            }else{
              Post.remove({id:post_id},function(err,msg){
                if (err) throw err;
                else{
                  res.json({success:true,msg:msg});
                }
              })
            }
          })
        }
      });
    }
  });
});

router.post("/editUser/",function(req,res,next){
  var user_details = req.body;
  console.log(user_details);
  User.update({id:user_details.id},{$set:{
    fname:user_details.fname,
    lname:user_details.lname,
    dname:user_details.dname,
    phone:user_details.phone,
    age:user_details.age,
    bio:user_details.bio
  }},function(err,out){
    if (err){
      res.json({success:false,msg:"Some kind of error"});
    }
    else{
      res.json({success:true,msg:out})
    }
  })
});

router.get("/favore/:post_id",function(req,res,next){
  var post_id = req.params.post_id;
  Post.find({id:post_id},function(err,pst){
    if (err){
      res.json({success:"False",msg:"Some kind of error"})
    }
    else if (pst == null){
      res.json({success:"False",msg:"No such post"})
    }
    else {
    var post = new Post({
      postText:pst.postText,
      id:pst.id,
      location:pst.location,
      favors:pst.favors,
      age:pst.age,
      date:pst.date
    });
    post.favored();
    Post.update({"_id":post_id},post,function(err,result){
      if (err){
        res.json({success:false,msg:"some kind of error"})
      }else{
        res.json({success:true,res:result})
      }
    });
  }
  })

});

router.get("/addFriend/",function(req,res,next){
  /*
    usage: localhost:3000/user/fetchPosts?usr=xx&frnd=xx
  */
  var user_id = req.query.usr;
  var frnd_id = req.query.frnd;
  if (user_id == frnd_id){
    res.json({success:false,msg:"Same user dumbo"});
  }
  else{
  // Check if the frind exists
  User.findOne({id:frnd_id},function(err,user){
    if (err){
      res.json({success:false,msg:"Some kind of the error!"});
    }
    else if (user==null){
      res.json({success:false,msg:"No such frind"})
    }
    else{
      User.findOne({id:user_id},function(err,ur){
        if (err){
          res.json({success:false,msg:"Some kind of the error!"});
        }else if (ur.friends.indexOf(frnd_id) != -1) {
          res.json({success:false,msg:"Already a friend"});
        }else{
          var friends_list = ur.friends;
          friends_list.push(frnd_id);
          User.update({id:user_id},{$set:{friends:friends_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error"});
            }
            else res.json({success:true,msg:out});
          });
        }
      });
    }
  });
}
});

router.get("/removeFriend/",function(req,res,next){
  var user_id = req.query.usr;
  var frnd_id = req.query.frnd;
  if (user_id == frnd_id){
    res.json({success:false,msg:"Same user dumbo"});
  }
  else{
  // Check if the frind exists
  User.findOne({id:frnd_id},function(err,user){
    if (err){
      res.json({success:false,msg:"Some kind of the error!"});
    }
    else if (user==null){
      res.json({success:false,msg:"No such friend"})
    }
    else{
      User.findOne({id:user_id},function(err,ur){
        if (err){
          res.json({success:false,msg:"Some kind of the error!"});
        }else if (ur.friends.indexOf(frnd_id) == -1) {
          res.json({success:false,msg:"Not a friend"});
        }else{
          var friends_list = ur.friends;

          friends_list.splice(friends_list.indexOf(frnd_id,1));
          console.log(friends_list);
          User.update({id:user_id},{$set:{friends:friends_list}},function(err,out){
            if (err){
              res.json({success:false,msg:"Some kind of error"});
            }
            else res.json({success:true,msg:out});
          });
        }
      });
    }
  });
}
});

router.get("/getFriendList/:user_id",function(req,res,next){
  User.findOne({id:req.params.user_id},function(err,user){
    if(err){
      res.json({success:false,msg:"some kind of error"});
    }else{
      res.json({success:true,friends:user.friends});
    }
  });
});

router.get("/getFriendList/:user_id",function(req,res,next){
  User.remove({id:req.params.user_id},function(err,out){
    if(err){
      res.json({success:false,msg:"some kind of error"});
    }else{
      res.json({success:true,msg:"Successfully deleted user"});
    }
  })

});

module.exports = router;
