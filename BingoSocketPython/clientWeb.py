import asyncio
import json
from io import StringIO
import socket
import websockets
from websockets import connect
from threading import Lock, Thread

lock = Lock()

message_list = []  # global


# nhận thông tin về game
async def ws_game():
    global message_list
    bln_running = True
    # ws_a = websockets.connect("ws://127.0.0.1:10637/game/bingo")
    async with connect("ws://127.0.0.1:10637/game/bingo") as ws_a:
        print(ws_a)
        io = StringIO()
        data = {
            'type': 14,
            'select': 0,
            'sender': "python",
            'table': [-1]
        }
        json.dump(data, io)
        print(io.getvalue())

        await ws_a.send(io.getvalue())

        while bln_running:
            response_a = await ws_a.recv()
            # dataRecv = json.loads(msg)
            print(response_a)
            lock.acquire()
            message_list.append(response_a)
            lock.release()


# Gửi thông tin game cho web
async def ws_web():
    global message_list
    bln_running = True
    # ws_b = websockets.connect("ws://127.0.0.1:12345/")
    # async with websockets.connect("ws://104.194.240.16/ws/channels/") as ws_b:
    async with websockets.connect("ws://127.0.0.1:12345/") as ws_b:
        print(ws_b)
        lenArray = len(message_list)
        sevSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sevSocket.bind(('localhost', 12346))
        print('socket serve start')
        sevSocket.listen(5)
        cli, ip = sevSocket.accept()

        while bln_running:
            # if lenArray != len(message_list):
            msg = cli.recv(1024).decode()
            print(msg)
            obj = json.loads(msg)
            print('result: ' + str(obj['result']))
            print('id1: ' + str(obj['id1']))
            print('id2: ' + str(obj['id2']))
            objSend = {}
            if obj['result'] == 7:
                objSend = {
                    'result': 3,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 2
                }
            elif obj['result'] == 12:
                objSend = {
                    'result': 2,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 1
                }
            elif obj['result'] == 3:
                objSend = {
                    'result': 1,
                    'id1': obj['id1'],
                    'id2': obj['id2'],
                    'match': obj['match'],
                    'status': 1
                }
            io = StringIO()
            json.dump(objSend, io)
            message_list.append(msg)
            # lock.acquire()
            await ws_b.send(io.getvalue())
            # lock.release()


def between_callback_web():
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)

    loop.run_until_complete(ws_web())
    loop.close()


# ------------main-------------
threads = []

for func in [between_callback_web()]:
    threads.append(Thread(target=func))
    # threads[-1].start()

for i in threads:
    i.start()
    i.join()
