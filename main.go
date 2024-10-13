package main

import (
	"math/big"
)

// 辅助函数：从字节数组转换为BigInteger
func fromUnsignedByteArray(data []byte) *big.Int {
	result := new(big.Int)
	result.SetBytes(data) // 注意：这是有符号的，但在这个上下文中，我们假设输入是无符号的
	return result
}

func main() {

}
