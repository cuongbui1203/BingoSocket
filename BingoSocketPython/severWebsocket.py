import asyncio
import websockets
import json

session = set()
PORT = 8887


# sever handle

async def echo(websocket):
    print("connect")
    async for message in websocket:
        # pass
        print(message)
        # recv = json.loads(message)
        # print(recv)
        # await websocket.send(message)


# -------------------------------------------------
async def main():
    async with websockets.serve(echo, "0.0.0.0", 14255):
        await asyncio.Future()  # run forever


asyncio.run(main())
