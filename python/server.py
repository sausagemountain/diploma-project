from types import FunctionType
from flask import Flask, request, Response, jsonify
import base64
import random

app = Flask(__name__)

all_objects = dict()


def run():
    app.run()


def generate_id(length: int):
    data = bytearray()
    for i in range(length):
        data.append(random.randrange(0,256))
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

        id = None
        while id is not None and ' '.join([class_name, id]) not in all_objects.keys():
            id = generate_id(10)
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
        id = request.json['id']
        internal_id = ' '.join([class_name, id])
        object = request.json['object']
        if object is not None:
            all_objects[internal_id] = object
        else:
            object = all_objects[internal_id]

        arguments = request.json['arguments']

        clazz = [i for i in registered_methods.keys() if class_name == str(i)][0]
        method = [i for i in registered_methods[clazz] if method_name == str(i)][0]
        if object is not None:
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


registered_methods = dict()


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
registered_guis= dict()


def register_module(name: str, url: str, guiUrl = ''):
    if guiUrl.strip() != '':
        registered_guis[name] = guiUrl.strip()
    if url.strip() != '':
        registered_modules[name] = url.strip()


def remove_module(name: str):
    registered_modules.pop(name)
    registered_guis.pop(name)
    pass


register_method(None, register_module)
register_method(None, remove_module)


@app.route('/gui/all')
def get_guis():
    return registered_guis


if __name__ == '__main__':
    run()
