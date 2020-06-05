using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace c_sharp_interop.Controllers
{
    [Route("/gui/")]
    [ApiController]
    public class GuiController: ControllerBase
    {
        [HttpGet("/here")]
        public IDictionary<string, string> GetHere()
        {
            return Registrator.Instance.GuiList();
        }


        [HttpGet("/all")]
        public IDictionary<string, string> GetAll()
        {
            var all = new Dictionary<string, string>();
            foreach ((string key, string value) in Registrator.Instance.GuiList())
                all.Add(key, value);
            string[] lines = System.IO.File.ReadAllLines("gui_uris.conf").
                                    Concat(Registrator.Instance.Modules.Keys).
                                    ToArray();
            foreach (string line in lines) {
                var client = new HttpClient();
                Task<HttpResponseMessage> task = client.
                    GetAsync(new Uri(new Uri(line), new Uri("/gui/here")));
                task.Wait();

                HttpResponseMessage message = task.Result;
                Task<string> resultTask = message.Content.ReadAsStringAsync();
                resultTask.Wait();

                var response = (IDictionary<string, string>) JsonSerializer.
                    Deserialize(resultTask.Result, typeof (IDictionary<string, string>));
                foreach ((string key, string value) in response)
                    all.Add(key, value);
            }

            return Registrator.Instance.GuiList();
        }
    }
}