using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace c_sharp_interop.Controllers {
    [ApiController]
    [Route("/")]
    public class HomeController
    {
        private Dictionary<string, object> _allObjects = new Dictionary<string, object>(){{"",null}};
        private Random rand = new Random();

        [HttpPost("/{className}")]
        public object Object(string className, Dictionary<string, object> input = null)
        {
            var response = new Dictionary<string, object>();

            if (input == null) {
                return null;
            }
            
            try {
                bool hasId = input.TryGetValue("id", out object idObj);
                if (hasId) {
                    if (idObj is string id) {
                        response["id"] = id;
                        response["result"] = _allObjects[" ".Join(className, id)];
                    }
                }
                else {
                    string id;
                    do {
                        id = rand.NextString(10);
                    } while (_allObjects.ContainsKey(" ".Join(className, id)));
                    response["id"] = id;
                    object obj = null;
                    if (input.TryGetValue("arguments", out object argumentsObj)) {
                        obj = Type.GetType(className)?.GetConstructor(new Type[] { })?.Invoke((object[])argumentsObj);
                    }
                    else {
                        obj = Type.GetType(className)?.GetConstructor(new Type[] { })?.Invoke(new object[]{ });
                    }

                    _allObjects[" ".Join(className, id)] = obj;
                    response["result"] = obj;
                }
            }
            catch (Exception e) {
                Console.WriteLine(e);
                response["error"] = "cannot complete operation";
            }
            return response;
        }

        [HttpPost("/{className}/{methodName}")]
        public object Method(string className, string methodName, Dictionary<string, object> input = null)
        {
            var response = new Dictionary<string, object>();

            if (input == null) {
                return null;
            }
            
            try {
                object @object;
                string id;
                if (input.TryGetValue("id", out object idObj)) {
                    id = idObj as string;
                    if (input.TryGetValue("object", out @object)) {
                        _allObjects[" ".Join(className, id)] = @object;
                    }
                    else {
                        _allObjects.TryGetValue(" ".Join(className, id), out @object);
                    }
                }
                else {
                    if (input.TryGetValue("object", out @object)) {
                        do {
                            id = rand.NextString(10);
                        } while (_allObjects.ContainsKey(" ".Join(className, id)));
                        _allObjects[" ".Join(className, id)] = @object;
                    }
                    else {
                        throw new ArgumentException();
                    }
                }
                input.TryGetValue("arguments", out object argumentsObj);

                var arguments = argumentsObj as object[];
                if (Type.GetType(className).IsInstanceOfType(@object))
                    response["result"] = Type.GetType(className)?.GetMethod(methodName)?.Invoke(@object, arguments);
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