package main

import (
	"crypto/ecdsa"
	"crypto/elliptic"
	"crypto/rand"
	"crypto/sha256"
	"fmt"
	"log"
)

// secp256r1

func main() {

	// 解析私钥
	// 生成 secp256r1 曲线上的 ECDSA 私钥
	privateKey, err := ecdsa.GenerateKey(elliptic.P256(), rand.Reader)
	if err != nil {
		log.Fatalf("无法生成密钥对: %v", err)
	}

	// 要签名的消息
	message := []byte("This is a message to be signed")
	hash := sha256.Sum256(message)

	// 使用私钥进行签名
	r, s, err := ecdsa.Sign(rand.Reader, privateKey, hash[:])
	if err != nil {
		fmt.Println("Error signing message:", err)
		return
	}

	// 打印签名（通常你会将其发送或存储在某处）
	fmt.Printf("Signature: (r: %x, s: %x)\n", r, s)

	// ... 在这里，你可以将签名和消息发送给需要验证的接收方 ...
}
