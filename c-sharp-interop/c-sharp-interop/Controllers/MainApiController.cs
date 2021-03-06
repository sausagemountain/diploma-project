﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using System.Threading;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;

namespace c_sharp_interop.Controllers
{
    [Route("api")]
    [ApiController]
    public class MainApiController
    {
        static MainApiController()
        {
            _cleaner = new Timer(
                obj => { AllObjects.Clear(); },
                null,
                new TimeSpan(1, 0, 0),
                new TimeSpan(1, 0, 0)
            );
        }

        private static Timer _cleaner;

        private static readonly Random Rand = new Random();

        private static Dictionary<string, object> AllObjects { get; } =
            new Dictionary<string, object> { { " ".Join(nameof (Registrator), "0"), Registrator.Instance } };

        [HttpPost("{className}")]
        public object Object(string className, MethodCall input = null)
        {
            var response = new Dictionary<string, object>();

            if (input == null)
                return null;

            try {
                if (input.Id != null) {
                    response["id"] = input.Id;
                    response["result"] = AllObjects[" ".Join(className, input.Id)];
                }
                else {
                    object obj = null;
                    Type type = Registrator.Instance.LocalClasses.First(p => p.Key.Name == className).Key;
                    if (input.Arguments != null)
                        obj = type.GetConstructor(input.Arguments.Select(i => new JObject(i))
                                                       .Select(e => e.GetType()).ToArray())?.
                                   Invoke(input.Arguments.Select(i => new JObject(i)).Cast<dynamic>().ToArray());
                    else
                        obj = type.GetConstructor(new Type[] { })?.Invoke(new object[] { });

                    string id;
                    do {
                        id = Rand.NextString(10);
                    } while (AllObjects.ContainsKey(" ".Join(className, id)));

                    response["id"] = id;

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

        [HttpPost("{className}/{methodName}")]
        public object Method(string className, string methodName, MethodCall input = null)
        {
            var response = new Dictionary<string, object>();

            if (input == null)
                return null;
            try {
                dynamic @object = input.Object;
                if (input.Id != null) {
                    if (input.Object != null) {
                        AllObjects[" ".Join(className, input.Id)] = input.Object;
                    }
                    else {
                        AllObjects.TryGetValue(" ".Join(className, input.Id), out object o);
                        @object = o;
                    }
                }
                else {
                    if (input.Object != null) {
                        do {
                            input.Id = Rand.NextString(10);
                        } while (AllObjects.ContainsKey(" ".Join(className, input.Id)));

                        AllObjects[" ".Join(className, input.Id)] = input.Object;
                    }
                    else {
                        throw new Exception();
                    }
                }
                Type classType = Registrator.Instance.LocalClasses.First(p => p.Key.Name == className).Key;
                if (Registrator.Instance.LocalClassNames[className].Contains(methodName)) {
                    response["result"] =
                        Registrator.Instance.LocalClasses[classType].First(e => e.Name == methodName).
                                    Invoke(
                                        @object,
                                        input.Arguments.Select(i => new JObject(i)).Cast<dynamic>().ToArray()
                                    );
                    response["object"] = input.Object;
                }
            }
            catch (Exception e) {
                Console.WriteLine(e);
                response["error"] = "cannot complete operation";
            }

            return response;
        }

        public class MethodCall
        {
            [JsonPropertyName("id")] public string Id { get; set; }
            [JsonPropertyName("arguments")] public JArray Arguments { get; set; }
            [JsonPropertyName("object")] public JObject Object { get; set; }
        }
    }
}