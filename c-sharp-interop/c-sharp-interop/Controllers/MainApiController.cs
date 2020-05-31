using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Microsoft.AspNetCore.Mvc;

namespace c_sharp_interop.Controllers {
    [ApiController]
    [Route("/api/")]
    public class MainApiController
    {
        private Dictionary<string, object> AllObjects { get; } =
            new Dictionary<string, object>() { { " ".Join(nameof (Registrator), "0"), Registrator.Instance } };
        private Random rand = new Random();

        private IEnumerable<MethodInfo> LocalMethods => Registrator.Instance.LocalMethods;
        private IDictionary<string, IEnumerable<string>> LocalClasses => Registrator.Instance.LocalClasses;

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
                        response["result"] = AllObjects[" ".Join(className, id)];
                    }
                }
                else {
                    string id;
                    do {
                        id = rand.NextString(10);
                    } while (AllObjects.ContainsKey(" ".Join(className, id)));
                    response["id"] = id;
                    object obj = null;
                    if (input.TryGetValue("arguments", out object argumentsObj)) {
                        obj = Type.GetType(className)?.GetConstructor(new Type[] { })?.Invoke((object[])argumentsObj);
                    }
                    else {
                        obj = Type.GetType(className)?.GetConstructor(new Type[] { })?.Invoke(new object[]{ });
                    }

                    AllObjects[" ".Join(className, id)] = obj;
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
                        AllObjects[" ".Join(className, id)] = @object;
                    }
                    else {
                        AllObjects.TryGetValue(" ".Join(className, id), out @object);
                    }
                }
                else {
                    if (input.TryGetValue("object", out @object)) {
                        do {
                            id = rand.NextString(10);
                        } while (AllObjects.ContainsKey(" ".Join(className, id)));
                        AllObjects[" ".Join(className, id)] = @object;
                    }
                    else {
                        throw new ArgumentException();
                    }
                }
                input.TryGetValue("arguments", out object argumentsObj);

                var arguments = argumentsObj as object[];
                var classType = Type.GetType(className);
                if (LocalClasses.ContainsKey(className) && LocalClasses[className].Contains(methodName) && classType.IsInstanceOfType(@object)) {
                    response["result"] = LocalMethods.First(m => m.DeclaringType.Name == className && m.Name == methodName)?.
                                                      Invoke(@object, arguments);
                    response["object"] = @object;
                }
            }
            catch (Exception e) {
                Console.WriteLine(e);
                response["error"] = "cannot complete operation";
            }
            return response;
        }
    }
}