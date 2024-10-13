package main

import (
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"crypto/x509"
	"encoding/pem"
	"fmt"
	"log"

	"sam-mix.com/x/embedx"
)

func loadECDSAPrivateKeyFromPEM(pemBytes []byte) (*ecdsa.PrivateKey, error) {
	// Decode the PEM block containing the private key
	block, _ := pem.Decode(pemBytes)
	if block == nil || block.Type != "EC PRIVATE KEY" {
		return nil, fmt.Errorf("failed to decode PEM block containing EC PRIVATE KEY")
	}

	// Parse the private key
	privateKey, err := x509.ParseECPrivateKey(block.Bytes)
	if err != nil {
		return nil, err
	}

	return privateKey, nil
}

func main() {
	// Load the ECDSA private key from the PEM file
	privateKey, err := loadECDSAPrivateKeyFromPEM(embedx.GetPrivKey())
	if err != nil {
		log.Fatalf("failed to load ECDSA private key: %v", err)
	}

	// Print the private key (for demonstration purposes only; do not do this in production)
	fmt.Printf("Loaded ECDSA private key: %+v\n", privateKey)

	// You can now use the privateKey for signing operations
	// ...
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
}
