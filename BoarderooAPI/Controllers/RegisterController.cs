using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]

 public class RegisterController:ControllerBase
 {
    private readonly RegisterService _registerService;
    public RegisterController(RegisterService registerService)
    {
        this._registerService=registerService;
    }

    [HttpPost(Name = "Register")]
    public async Task<ActionResult> Register(UserDocument user)
    {
        var response=await _registerService.Register(user); 
        return ConvertServiceResultToActionResult(response);
        //return Ok();
    }

    [HttpPost]
    [Route("/verify/{token}")]
    public async Task<ActionResult> Verify(string token)
    {
        var response=await _registerService.Verify(token);
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