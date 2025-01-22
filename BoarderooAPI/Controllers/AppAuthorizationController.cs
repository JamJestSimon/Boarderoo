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
    public async Task<ActionResult> GetDiscordUser(string token)
    {
        var response=await _discordService.GetDiscordUserInfo(token);
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
