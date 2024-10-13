package embedx

import (
	_ "embed"
	"log"
)

// 使用 //go:embed 指令嵌入 assets/private_key.pem 文件
//
//go:embed assets/private_key.pem
var privateKey string

// 使用 //go:embed 指令嵌入 assets/public_key.pem 文件
//
//go:embed assets/public_key.pem
var publicKey string

func Println() {
	log.Println(privateKey)
	log.Println("======================================================")
	log.Println(publicKey)
}

func GetPrivKey() []byte {
	return []byte(privateKey)
}
