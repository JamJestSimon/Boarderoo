using System.Collections.ObjectModel;
using BoarderooAPI.Model;
using BoarderooAPI.Service;
using FirebaseAdmin.Auth;
using Google.Cloud.Firestore;
using Google.Protobuf.WellKnownTypes;
using Microsoft.AspNetCore.Mvc;
namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]
public class UserController: ControllerBase
{
    private readonly FireBaseService _firebaseService;
    
    public UserController(FireBaseService firebaseService)
    {
        _firebaseService = firebaseService;
    }

    [HttpGet(Name = "GetUsers")]
    public async Task<ActionResult> GetAllUsers()
    {
        var response=await _firebaseService.GetAllUsers(); 
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost(Name = "PostUser")]
    public async Task<ActionResult> AddUser(User user)
    {
            var response=await _firebaseService.AddUser(user);
            return ConvertServiceResultToActionResult(response);
        
    }

    [HttpDelete]
    [Route("{email}")]
    public async Task<ActionResult> DeleteUser(string email)
    {

            var response=await _firebaseService.DeleteUser(email);
            return ConvertServiceResultToActionResult(response);

    }


    [HttpPut]
    [Route("{email}")]
    public async Task<ActionResult> UpdateUser(string email,GeoPoint Location,string name,string password,string surname)
    {
           var response=await _firebaseService.UpdateUser(email,Location,name,password,surname);
            return ConvertServiceResultToActionResult(response);
    }
    
    [HttpGet]
    [Route("{email}")]
    public async Task<ActionResult> GetUser(string email)
    {
        var response=await _firebaseService.GetUserByEmail(email);
        return ConvertServiceResultToActionResult(response);
    }
    private ActionResult ConvertServiceResultToActionResult<T>(UserServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult.Data); // Sukces
    }

    if (serviceResult.ResultCode is not null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(new { Message = serviceResult.Message });
}
    // [HttpGet(Name = "GetLogin")]
    // public async Task<ActionResult> Login(string email,string password)
    // {
    //     try
    //     {
    //         string hashedpassword = HashService.hashfunction(password);
    //         var usersCollection = _firestoreDB.Collection("users");
            
    //         return Ok();
    //     }
    //     catch (FirebaseAuthException e)
    //     {
    //         return BadRequest(new {Message=e.Message});
    //     }
    // }
    // [HttpDelete]
    // public async Task<ActionResult> DeleteUser(User user)
    // {
    //     return Ok();
    // }
}