import os

try:
    import asyncio
    import json
    import socket
    import websockets
except ImportError:
    print("Trying to Install required module: requests\n")
    os.system('python -m pip install asyncio websockets json')
# -- above lines try to install requests module if not present
# -- if all went well, import required module again ( for global access)

import asyncio
import json
import socket
import websockets


# Gửi thông tin game cho web
async def ws_web():
    print("start new loop")
    bln_running = True
    sevSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sevSocket.bind(('localhost', 12346))
    print('socket serve start')
    sevSocket.listen(5)
    cli, ip = sevSocket.accept()
    print(cli)

    msg = cli.recv(1024).decode()
    # ws_b = websockets.connect("ws://127.0.0.1:12345/")
    async with websockets.connect("ws://104.194.240.16/ws/channels/") as ws_b:
        # async with websockets.connect("ws://127.0.0.1:12345/") as ws_b:
        print(ws_b)

        while bln_running:
            # if lenArray != len(message_list):
            print(msg)
            obj = json.loads(msg)
            print('result: ' + str(obj['result']))
            print('id1: ' + str(obj['id1']))
            print('id2: ' + str(obj['id2']))
            objSend = {}
            if obj['result'] == 7:
                # End
                objSend = {
                    'result': 3,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 2
                }
                bln_running = False
            elif obj['result'] == 12:
                # Update Score
                objSend = {
                    'result': 2,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 1
                }
            elif obj['result'] == 3:
                # Start
                objSend = {
                    'result': 1,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 1
                }
            # lock.acquire()

            try:
                await ws_b.send(json.dumps(objSend))
                print(await ws_b.recv())
                if obj['result'] == 7:
                    print("end")
                    return
            except:
                pass
            msg = cli.recv(1024).decode()
            # lock.release()S

        # ws_b.close()


def between_callback_web():
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)

    loop.run_until_complete(ws_web())
    loop.close()


# ------------main-------------
asyncio.run(ws_web())
# loop = asyncio.new_event_loop()

# asyncio.set_event_loop(loop=loop)

# loop.run_until_complete(ws_web())
# loop.run_forever()
# loop.
