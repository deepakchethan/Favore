const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');
const autoincrement = require('simple-mongoose-autoincrement')
var Schema = mongoose.Schema;

var UserSchema = new Schema({
  id:{
    type:Schema.Types.ObjectId
  },
  username:{
    type:String,
    require: true
  },
  password:{
    type:String,
    required: true
  },
  age:{
    type:Number
  },
  fname:{
    type:String
  },
  lname:{
    type:String
  },
  dname:{
    type:String
  },
  phone:{
    type:Number
  },
  bio:{
    type:String
  },
  friends:{
    type:[Number]
  },
  posts:{
    type:[Number]
  }
});

UserSchema.pre('save',function(next){
  var user = this;
  if (this.isModified('password') || this.isNew){
    bcrypt.genSalt(10, function(err,salt){
      if (err){
        return next(err);
      }
      bcrypt.hash(user.password,salt,null,function(err,hash){
        if (err){
          return next(err);
        }
        user.password = hash;
        next();
      });
    });
  }
});

UserSchema.methods.comparePassword = function(passw,cb){
  bcrypt.compare(passw,this.password,function(err,isMatch){
    if (err){
      return cb(err);
    }
    cb(null,isMatch);
  })
}

mongoose.plugin(autoincrement,{field:'id'});
module.exports = mongoose.model('User',UserSchema);
