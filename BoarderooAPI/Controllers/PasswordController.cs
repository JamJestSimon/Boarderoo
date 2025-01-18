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
public class PasswordController : ControllerBase
{
    private readonly FireBaseService _firebaseService;
    private readonly UserService _userService;
    private readonly GameService _gameService;

    public PasswordController(FireBaseService firebaseService, UserService userService, GameService gameService)
    {
        _firebaseService = firebaseService;
        _userService = userService;
        _gameService = gameService;


    }

    [HttpGet("reset")]
    public async Task<ActionResult>ResetPasswordValidate(string email)
    {
        var response = await _userService.ResetPasswordValidate(email);
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost("resetPassword")]
    public async Task<ActionResult>ResetPassword([FromBody]ResetPasswordRequest reset)
    {
        var response = await _userService.ResetPassword(reset.Password,reset.Token);
        return ConvertServiceResultToActionResult(response);
    }

    private ActionResult ConvertServiceResultToActionResult<T>(ServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult); // Sukces
    }

    if (serviceResult.ResultCode is null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(serviceResult);
}
}