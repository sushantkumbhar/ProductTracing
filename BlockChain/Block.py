import hashlib

# class block:
#     def __init__(self,previous_hash,transction):
#         self.transaction=transction
#         self.previous_hash=previous_hash
#         string_to_hash="".join(transction)+previous_hash
#         print("String_To_hash:",string_to_hash)
#         self.block_hash=hashlib.sha256(string_to_hash.encode()).hexdigest()
#         #print(self.__dict__)
#############################################################################################
import pyqrcode
import png
from pyqrcode import QRCode
import json
from datetime import datetime
from hashlib import sha256
import pymongo
from flask import Flask, render_template, request,jsonify, make_response,request
# from flask_cors import CORS,cross_origin
import requests

app = Flask(__name__)  # initialising the flask app with the name 'app'

class Block:
    def __init__(self,index,previous_hash,current_transction,timestamp,nonce):
        self.index=index
        self.previous_hash = previous_hash
        self.current_transction=current_transction
        self.timestamp=timestamp
        self.nonce=nonce
        self.hash=self.compute_hash()
        #print(self.__dict__)

    def compute_hash(self):
        block_string=json.dumps(self.__dict__,sort_keys=True)
        first_hash=sha256(block_string.encode()).hexdigest()
        second_hash = sha256(first_hash.encode()).hexdigest()
        print(second_hash)
        return second_hash

    def __str__(self):
        return str(self.__dict__)

class BlockChain:
    def __init__(self):
        self.chain=[]
        self.transactions=[]
        self.genesis_block()

    def __str__(self):
        return str(self.__dict__)

    def genesis_block(self):
        genesis_block=Block('Genesis',0x0,[3,4,5,6,7],datetime.now().strftime("%d/%m/%Y, %H:%M:%S"),0)
        genesis_block.hash=genesis_block.compute_hash()
        self.chain.append(genesis_block.hash)
        self.transactions.append(str(genesis_block.__dict__))
        print('Genesis Block is Ready')
        return genesis_block

    def getLastBlock(self):
        return self.chain[-1]

    def proof_of_work(self,block):
        difficulty=1
        block.nonce=0
        computed_hash=block.compute_hash()
        while not(computed_hash.endswith('0'* difficulty) and str(55 * difficulty) in (computed_hash)):
            block.nonce+=1
            computed_hash=block.compute_hash()
        return computed_hash

    def add(self,data):
        block=Block(len(self.chain),self.chain[-1],data,datetime.now().strftime("%d/%m/%Y, %H:%M:%S"),0)
        block.hash=self.proof_of_work(block)
        print(block.hash)
        self.chain.append(block.hash)
        self.transactions.append(block.__dict__)
        #return json.load(str(block.__dict__).replace('\'','\"'))

    def getTransactions(self,id):
        labels=['Manufacturer','Transportation','Retailer']
        #print(self.transactions)
        while True:
            try:
                if id=='all':
                    for i in range(len(self.transactions)-1):
                        print('{}:\n{}\n'.format(labels[i],self.transactions[i+1]))
                    break
                elif type(id)==int:
                    print(self.transactions[id])
                    break
            except Exception as e:
                print(e)
        return self.transactions
#Class BlockChain Ends here

class dbconfig:
    def __init__(self):
        print("Initialize DB")
        USR = "admintest"  # User created by us
        PWD = "w2UfXGmvpy0P6Mif"

        DB_NAME = "BLOCKCHAINDB"  # Specifiy a Database Name

        # Connection URL
        CONNECTION_URL = f"mongodb+srv://admintest:w2UfXGmvpy0P6Mif@cluster0.u9n5v.mongodb.net/testdb?retryWrites=true&w=majority"
        # mongodb+srv://<username>:<password>@cluster0.u9n5v.mongodb.net/<dbname>?retryWrites=true&w=majority

        # Establish a connection with mongoDB
        client = pymongo.MongoClient(CONNECTION_URL)

        # Create a DB
        dataBase = client[DB_NAME]

        # Create a Collection Name
        COLLECTION_NAME = "Products"
        self.collection = dataBase[COLLECTION_NAME]

    def insertToDB(self, data):
        # print("data:",data)
        #data1 = {"data": "[{'index': 1, 'previous_hash': '6ebc2f46a3222a75c1c8bb66ad1a0d69c27f89e65fb713e96c9903eca6c481ba', 'current_transction': {'transactions': [{'timestamp': '18/09/2020, 20:29:05', 'product_id': 1, 'product_serial': 5000100, 'name': 'pant', 'from': 'manufacturer x', 'to': 'transportation x'}]}, 'timestamp': 'datetime.now().timestamp()', 'nonce': 39, 'hash': '3906ff2a0a7fb4e4b0d71ff18aa89f80cf7c755c873cbc2fdd9c3b2b811d8c30'}, {'index': 2, 'previous_hash': '3906ff2a0a7fb4e4b0d71ff18aa89f80cf7c755c873cbc2fdd9c3b2b811d8c30', 'current_transction': {'transactions': [{'timestamp': '18/09/2020, 20:29:05', 'product_id': 1, 'product_serial': 5000100, 'name': 'pant', 'from': 'transportation x', 'to': 'retailer x'}]}, 'timestamp': 'datetime.now().timestamp()', 'nonce': 7, 'hash': '3f1927d9a31de52c120db654775535bbe53b63be2d479a0e22bdcb4a303a6600'}, {'index': 3, 'previous_hash': '3f1927d9a31de52c120db654775535bbe53b63be2d479a0e22bdcb4a303a6600', 'current_transction': {'transactions': [{'timestamp': '18/09/2020, 20:29:05', 'product_id': 1, 'product_serial': 5000100, 'name': 'pant', 'from': 'retailer x', 'to': 'shelf'}]}, 'timestamp': 'datetime.now().timestamp()', 'nonce': 19, 'hash': '2554e65ab53ff90e835e0438e729756853aab7043f2c502a08838fd4e28faa10'}]"}
        #print("data:", data)
        #print("data1:", data1)
        #print(type(data))
        #print(type(data1))
        self.collection.insert_one(data)

    def getblock(self,block_hash):
        resp_block=self.collection.find_one(block_hash)
        return resp_block


def createQrcode(hashcodeqr):
    # String which represents the QR code
    inputStringForQr = hashcodeqr
    # Generate QR code
    qrUrl = pyqrcode.create(inputStringForQr)
    # Create and save the png file naming "hashcodeqr.png"
    qrUrl.png('static/qrcodes/'+hashcodeqr+'.png', scale=6)


    #Flask Implementation
@app.route('/', methods=['GET'])
def homepage():
    return render_template('input.html')

@app.route('/addblocks', methods=['POST'])  # route with allowed methods as POST and GET# route with allowed methods as POST and GET
def main():
    manufacturer = {
        "transactions":
            [
                {
                    'timestamp': datetime.now().strftime("%d/%m/%Y, %H:%M:%S"),
                    'product_id': request.form["productid1"],
                    'product_serial': request.form["productSerialId1"],
                    'name': request.form["productName1"],
                    'from': request.form["originpt"],
                    'to': request.form["manufacturerpt1"],
                    "type": "manufacturer"
                }

            ]
    }
    transportation = {
        "transactions":
            [
                {
                    "timestamp": datetime.now().strftime("%d/%m/%Y, %H:%M:%S"),
                    "product_id": request.form["productid2"],
                    "product_serial": request.form["productSerialId2"],
                    "name": request.form["productName2"],
                    "from": request.form["manufacturerpt2"],
                    "to": request.form["distributorpt1"],
                    "type": "transportation"
                }

            ]
    }
    retailer = {
        "transactions":
            [
                {
                    "timestamp": datetime.now().strftime("%d/%m/%Y, %H:%M:%S"),
                    "product_id": request.form["productid3"],
                    "product_serial": request.form["productSerialId3"],
                    "name": request.form["productName3"],
                    "from": request.form["distributorpt2"],
                    "to": request.form["retailerpt"],
                    "type":"retailer"
                }

            ]
    }

    B=BlockChain()
    m=B.add(manufacturer)
    t=B.add(transportation)
    r=B.add(retailer)
    trxs = B.getTransactions('all')
    lastblock_hash=trxs[-1]['hash']
    #print(lastblock_hash)
    doc_body={}
    doc_body['_id'] = lastblock_hash
    doc_body['data'] = str(trxs[1:])

    d=dbconfig()
    d.insertToDB(doc_body)

    qrimg=createQrcode(lastblock_hash)
    qrImagePath='qrcodes/'+lastblock_hash+'.png'
    jsondata = jsonify(trxs[1:])
    return render_template('output.html',qrImagePath = qrImagePath, jsondata = jsondata)
    #return jsonify(trxs[1:])


@app.route('/getblocks', methods=['GET'])
def getblocks():
    input_hash = (request.args.get('hash')) # here we want to get the value of hash (i.e. ?hash=some-value)
    print(input_hash)
    d1 = dbconfig()
    resposne_block=d1.getblock(input_hash)
    #print(resposne_block)
    return jsonify(resposne_block['data'])

if __name__ == "__main__":
    #main()
    app.run(port=8000,debug=True) # running the app on the local machine on port 8000

