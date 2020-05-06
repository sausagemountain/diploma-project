using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace c_sharp_interop.Controllers {
    [ApiController]
    [Route("/")]
    public class HomeController
    {
        [HttpPost]
        public object Post(Dictionary<string, object> input = null)
        {
            var response = new Dictionary<string, object>();

            try {
                input.TryGetValue("class", out object @classObj);
                input.TryGetValue("object", out object @object);
                input.TryGetValue("method", out object methodObj);
                input.TryGetValue("arguments", out object argumentsObj);

                var @class = @classObj as string;
                var method = methodObj as string;
                var arguments = argumentsObj as object[];

                if (Type.GetType(@class).IsInstanceOfType(@object))
                    response["result"] = Type.GetType(@class).GetMethod(method).Invoke(@object, arguments);
            }
            catch (Exception e) {
                Console.WriteLine(e);
                response["error"] = "cannot complete operation";
            }
            return response;
        }

        [HttpGet]
        public object Get()
        {
            return new object[]
                   { "hello", "world" };
        }
    }
}