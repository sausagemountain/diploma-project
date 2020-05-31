using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace c_sharp_interop.Controllers
{
    [Route("/gui/")]
    [ApiController]
    public class GuiController : ControllerBase
    {
        [HttpGet("/all")]
        public IDictionary<string, string> GetAll()
        {
            return Registrator.Instance.GuiList();
        }
    }
}
