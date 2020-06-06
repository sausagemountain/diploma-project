from flask import Flask, request, jsonify

app = Flask(__name__)
all_objects = dict()
registered_methods = dict()


def generate_id(length: int):
    import base64
    import random
    data = bytearray()
    for i in range(length):
        data.append(random.randrange(0, 256))
    result = str(base64.b64encode(bytearray(data)), 'utf-8')
    return result


@app.route('/api/<class_name>', methods=['POST'])
def new_instance(class_name: str):
    try:
        arguments = request.json['arguments']

        clazz = [i for i in registered_methods.keys() if class_name == str(i)][0]
        if arguments:
            result = clazz(*arguments)
        else:
            result = clazz()

        while True:
            id = generate_id(10)
            if ' '.join([class_name, id]) not in all_objects.keys():
                break
        internal_id = ' '.join([class_name, id])

        all_objects[internal_id] = result

        return {
            'id': id,
            'result': result
        }
    except Exception as e:
        print(e)
        return {
            'error': 'cannot complete operation'
        }


@app.route('/api/<class_name>/<method_name>', methods=['POST'])
def call_method(class_name: str, method_name: str):
    try:
        id = request.json.get('id')
        internal_id = ' '.join([class_name, id])
        object = request.json.get('object', '')
        if object is not None:
            all_objects[internal_id] = object
        else:
            object = all_objects[internal_id]

        arguments = request.json.get('arguments')

        clazz = [i for i in registered_methods.keys() if
                 (class_name == i or (type(class_name) != type('') and class_name == i.__name__))][0]
        method = [i for i in registered_methods[clazz] if method_name == i.__name__][0]
        if type(object) == type(''):
            result = method(*arguments)
        elif object is not None:
            result = method(object, *arguments)
        else:
            return None

        return {
            'id': id,
            'result': result
        }
    except Exception as e:
        print(e)
        return {
            'error': 'cannot complete operation'
        }


def known_classes():
    return {str(t): [str(m) for m in l] for t, l in registered_methods}


def register_method(clazz, function):
    if clazz is None:
        clazz = type(None)
    n = registered_methods.get(clazz, [])
    n.append(function)
    registered_methods[clazz] = n


def remove_method(clazz, function):
    if clazz is None:
        clazz = type(None)
    n = registered_methods.get(clazz, [])
    n.remove(function)
    if len(n) == 0:
        registered_methods.pop(function)
    else:
        registered_methods[clazz] = n


registered_modules = dict()
registered_guis = dict()


def register_module(name: str, url: str, guiUrl=''):
    if guiUrl.strip() != '':
        registered_guis[name] = guiUrl.strip()
    if url.strip() != '':
        registered_modules[name] = url.strip()


def remove_module(name: str):
    registered_modules.pop(name)
    registered_guis.pop(name)
    pass


register_method('Registrator', register_module)
register_method('Registrator', remove_module)
register_method('Generator', generate_id)


@app.route('/gui/all')
def get_all_guis():
    import json
    import http.client as http_client
    all_guis = list()
    all_guis.extend(registered_guis)
    lines = list()
    try:
        with open("gui_uris.conf", 'rt') as file:
            lines = file.readlines()
    except OSError:
        pass
    list.extend(registered_modules.values())
    for url in lines:
        conn = http_client.HTTPConnection(url)
        conn.connect()
        conn.request('GET', f'/gui/here')
        all_guis.extend(json.loads(conn.getresponse().msg.get_payload()))
        conn.close()
    return jsonify(all_guis)


@app.route('/gui/here')
def get_guis():
    return jsonify(registered_guis)


def __periodic__(sch, interval: int, action, args=()):
    sch.enter(interval, 1, __periodic__, (interval, __periodic__, args))
    action(*args)


def __clear__():
    all_objects.clear()


def run():
    import sched
    import time
    sch = sched.scheduler(time.time, time.sleep)
    __periodic__(sch, 60 * 60, __clear__)
    sch.run(False)
    app.run()


if __name__ == '__main__':
    run()
