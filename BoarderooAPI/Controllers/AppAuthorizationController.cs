using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]
public class AppAuthorizationController:ControllerBase
{
     private readonly DiscordService _discordService;
     private readonly GoogleService _googleService;
     public AppAuthorizationController(DiscordService discordService, GoogleService googleService)
     {
        _discordService=discordService;
        _googleService=googleService;
     }

     [HttpGet]
     [Route("/discordtoken")]
    public async Task<ActionResult> GetDiscordUserToken(string token)
    {
        var response=await _discordService.GetDiscordToken(token);
        return ConvertServiceResultToActionResult(response);

    }
    [HttpGet]
     [Route("/discorduser")]
    public async Task<ActionResult> GetDiscordUserInfo(string token)
    {
        var response=await _discordService.GetDiscordUserInfo(token);
        return ConvertServiceResultToActionResult(response);

    }
    [HttpGet]
     [Route("/googletoken")]
    public async Task<ActionResult> GetGoogleUserToken(string code)
    {
        var response=await _googleService.GetGoogleToken(code);
        return ConvertServiceResultToActionResult(response);
    }
    [HttpGet]
     [Route("/googleuser")]
    public async Task<ActionResult> GetGoogleUserInfo(string token)
    {
        var response=await _googleService.GetGoogleUserInfo(token);
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
