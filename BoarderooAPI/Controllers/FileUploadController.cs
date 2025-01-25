using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]
public class FileUploadController:ControllerBase
{
      private readonly FileService _fileService;

    public FileUploadController(FileService fileService)
    {
        _fileService=fileService;
    }
    [HttpPost("fileupload")]
    public async Task<IActionResult> FileUpload(IFormFile file)
    {
        if (file == null || file.Length == 0)
            {
                return BadRequest("Nie przesłano pliku.");
            }
        var response = await _fileService.AddFile(file);
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
