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
    private readonly UserService _userService;
    private readonly GameService _gameService;
    
    public UserController(FireBaseService firebaseService,UserService userService,GameService gameService)
    {
        _firebaseService = firebaseService;
        _userService=userService;
        _gameService=gameService;


    }

    [HttpGet(Name = "GetUsers")]
    public async Task<ActionResult> GetAllUsers()
    {
        var response=await _userService.GetAllUsers(); 
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost(Name = "PostUser")]
    public async Task<ActionResult> AddUser(UserDocument user)
    {
            var response=await _userService.AddUser(user);
            return ConvertServiceResultToActionResult(response);
        
    }

    [HttpDelete]
    [Route("{email}")]
    public async Task<ActionResult> DeleteUser(string email)
    {

            var response=await _userService.DeleteUser(email);
            return ConvertServiceResultToActionResult(response);

    }


    [HttpPut]
    public async Task<ActionResult> UpdateUser(UserDocument user)
    {
           var response=await _userService.UpdateUser(user);
            return ConvertServiceResultToActionResult(response);
    }
    
    [HttpGet]
    [Route("{email}")]
    public async Task<ActionResult> GetUser(string email)
    {
        var response=await _userService.GetUserByEmail(email);
        return ConvertServiceResultToActionResult(response);
    }
    private ActionResult ConvertServiceResultToActionResult<T>(ServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult); // Sukces
    }

    if (serviceResult.ResultCode is not null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(serviceResult);
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